/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.core.domain.rate

import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.vladsaybulin.core.domain.repository.UserRateRepository
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateContext
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateValues

/**
 * This class is designed to automatically correct the user rating edits to improve UX.
 * Before use, call method [initialize] to configure the corrector
 * and load the [context] if it [loadedContext] was null.
 */
class UserRateCorrector @AssistedInject constructor(
    private val userRateRepository: Lazy<UserRateRepository>,
    @Assisted val titleType: EntryType,
    @Assisted val titleId: Long,
    @Assisted loadedContext: UserRateContext?
) {

    @AssistedFactory
    interface Factory {
        fun create(
            titleType: EntryType,
            titleId: Long,
            loadedContext: UserRateContext?
        ): UserRateCorrector
    }

    private var _context = loadedContext

    val context: UserRateContext
        get() = checkNotNull(_context) { "Context is not loaded" }

    /**
     * Available user rate statuses based on the current context and autocorrection settings.
     */
    val availableStatuses: List<UserRateStatus>
        get() = buildAvailableStatuses()

    /**
     * Initializes the corrector by loading the context if it was not provided.
     * This method should be called from a coroutine scope, such as [androidx.lifecycle.viewModelScope].
     */
    suspend fun initialize() {
        if (_context != null) return

        _context = userRateRepository.get().getRateContext(titleType, titleId)
    }

    /**
     * Creates a new [UserRateValues] object with default values based on the current context.
     */
    fun newUserRateValues(): UserRateValues {
        val initialStatus = if (context.titleStatus == EntryStatus.Anons) {
            UserRateStatus.Planned
        } else {
            UserRateStatus.None
        }

        return UserRateValues(
            status = initialStatus,
            episodes = 0,
            chapters = 0,
            volumes = 0,
            rewatches = 0,
            score = 0,
            text = ""
        )
    }

    /**
     * Calculates the changes in progress that should be applied when the user rate status changes.
     * @param prevStatus The previous user rate status.
     * @param newStatus The new user rate status.
     * @return A [Progress] object containing the new progress values, or null if no changes are needed.
     */
    fun onStatusChanged(
        prevStatus: UserRateStatus,
        newStatus: UserRateStatus
    ): Progress? {
        if (prevStatus == newStatus) return null

        if (newStatus == UserRateStatus.Completed) {
            return Progress(
                episodes = context.maxEpisodes,
                chapters = context.maxChapters,
                volumes = context.maxVolumes
            )
        }

        if (newStatus == UserRateStatus.Planned) {
            return Progress(
                episodes = 0,
                chapters = 0,
                volumes = 0
            )
        }

        if (isStartWatching(prevStatus, newStatus) || isStartRewatching(prevStatus, newStatus)) {
            return Progress(
                episodes = 0,
                chapters = 0,
                volumes = 0
            )
        }

        return null
    }

    /**
     * Calculates the new user rate status based on the changes in progress.
     * @param progress The new progress values.
     * @return The new [UserRateStatus], or null if no changes are needed.
     */
    fun onProgressChanged(
        currentStatus: UserRateStatus,
        progress: Progress
    ): UserRateStatus? {
        val (episodes, chapters, volumes) = progress

        if (episodes == context.maxEpisodes || chapters == context.maxChapters || volumes == context.maxVolumes) {
            return UserRateStatus.Completed
        }

        if (currentStatus != UserRateStatus.Planned) {
            return UserRateStatus.Watching
        }

        return null
    }

    private fun buildAvailableStatuses(): List<UserRateStatus> {
        val titleStatus = context.titleStatus
        return buildList {
            add(UserRateStatus.Planned)

            if (titleIsFinisher(titleStatus) || titleStatus == EntryStatus.Ongoing) {
                add(UserRateStatus.Watching)
                add(UserRateStatus.Rewatching)

                if (titleStatus != EntryStatus.Ongoing) {
                    add(UserRateStatus.Completed)
                }
            }

            add(UserRateStatus.OnHold)
            add(UserRateStatus.Dropped)
        }
    }

    private fun isStartWatching(prevStatus: UserRateStatus, newStatus: UserRateStatus) =
        prevStatus == UserRateStatus.Planned || newStatus == UserRateStatus.Watching

    private fun isStartRewatching(prevStatus: UserRateStatus, newStatus: UserRateStatus) =
        prevStatus == UserRateStatus.Completed || newStatus == UserRateStatus.Rewatching

    private fun titleIsFinisher(titleStatus: EntryStatus) = titleStatus == EntryStatus.Released ||
            titleStatus == EntryStatus.Paused ||
            titleStatus == EntryStatus.Discontinued
}
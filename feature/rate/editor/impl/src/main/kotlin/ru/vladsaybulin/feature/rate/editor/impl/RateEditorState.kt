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

package ru.vladsaybulin.feature.rate.editor.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import ru.vladsaybulin.core.domain.rate.Progress
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateContext
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateValues

/**
 * Creates and remembers a [RateEditorState] for the rate editor screen.
 * [RateEditorState] is saveable across configuration changes and process death.
 * @param titleType The type of the title (Anime or Manga).
 * @param rateValues The initial values of the user's rate.
 * @param context The context of the user's rate.
 * @param availableStatuses The list of available statuses for the user's rate.
 * @return A [RateEditorState] instance that holds the state of the rate editor screen
 */
@Composable
fun rememberRateEditorState(
    titleType: EntryType,
    rateValues: UserRateValues,
    context: UserRateContext,
    availableStatuses: List<UserRateStatus>
): RateEditorState {
    return rememberSaveable(rateValues, context, saver = RateEditorState.Saver) {
        RateEditorState(
            allStatuses = availableStatuses,
            initialStatusIndex = availableStatuses.indexOf(rateValues.status),
            initialEpisodes = counterInitialValue(EntryType.Anime, titleType, rateValues.episodes),
            initialChapters = counterInitialValue(EntryType.Manga, titleType, rateValues.chapters),
            initialVolumes = counterInitialValue(EntryType.Manga, titleType, rateValues.volumes),
            initialRewatchingCount = rateValues.rewatches ?: 0,
            initialScore = rateValues.score ?: 0,
            text = rateValues.text ?: "",
        )
    }.apply {
        allStatuses = availableStatuses
        context.run {
            episodes?.limit = maxEpisodes
            chapters?.limit = maxChapters
            volumes?.limit = maxVolumes
        }
    }
}

/**
 * State holder for the rate editor screen.
 */
class RateEditorState(
    var allStatuses: List<UserRateStatus>,
    initialStatusIndex: Int,
    initialEpisodes: Int?,
    initialChapters: Int?,
    initialVolumes: Int?,
    initialRewatchingCount: Int,
    initialScore: Int,
    text: String,
) {
    private val _statusIndex = mutableIntStateOf(initialStatusIndex)
    var statusIndex: Int
        get() = _statusIndex.intValue
        set(value) { _statusIndex.intValue = value }

    var status: UserRateStatus
        get() = allStatuses.getOrElse(statusIndex) { UserRateStatus.None }
        set(value) {
            val index = allStatuses.indexOf(value)
            if (index != -1) {
                statusIndex = index
            }
        }

    val episodes = initialEpisodes?.let { CounterState(it) }

    val chapters = initialChapters?.let { CounterState(it) }

    val volumes = initialVolumes?.let { CounterState(it) }

    val rewatchingCount = CounterState(initialRewatchingCount)

    private val _score = mutableIntStateOf(initialScore)
    var score: Int
        get() = _score.intValue
        set(value) { _score.intValue = value }

    val text = mutableStateOf(text)

    /** Converts the current state of the rate editor into a [UserRateValues] object. */
    fun toUserRateValues() = UserRateValues(
        status = status,
        episodes = episodes?.value,
        chapters = chapters?.value,
        volumes = volumes?.value,
        rewatches = rewatchingCount.value,
        score = score,
        text = text.value
    )

    companion object {
        val Saver = listSaver(
            save = { state ->
                listOf(
                    state.statusIndex,
                    state.episodes?.value,
                    state.chapters?.value,
                    state.volumes?.value,
                    state.rewatchingCount.value,
                    state.score,
                    state.text.value
                )
            },
            restore = { restoredList ->
                val statusIndex = restoredList[0] as Int
                val initialEpisodes = restoredList[1] as Int?
                val initialChapters = restoredList[2] as Int?
                val initialVolumes = restoredList[3] as Int?
                val initialRewatchingCount = restoredList[4] as Int
                val initialScore = restoredList[5] as Int
                val text = restoredList[6] as String

                RateEditorState(
                    allStatuses = emptyList(), // This will need to be set after restoration, as it cannot be saved in the state.
                    initialStatusIndex = statusIndex,
                    initialEpisodes = initialEpisodes,
                    initialChapters = initialChapters,
                    initialVolumes = initialVolumes,
                    initialRewatchingCount = initialRewatchingCount,
                    initialScore = initialScore,
                    text = text
                )
            }
        )
    }
}

/**
 * Updates the progress counters in the [RateEditorState] based on the provided [Progress] object.
 * @param progress The [Progress] object containing the new progress values.
 */
fun RateEditorState.setProgress(progress: Progress) {
    episodes?.value = progress.episodes
    chapters?.value = progress.chapters
    volumes?.value = progress.volumes
}

private fun counterInitialValue(expectedType: EntryType, actualType: EntryType, value: Int?): Int? {
    return if (expectedType == actualType) value ?: 0 else null
}
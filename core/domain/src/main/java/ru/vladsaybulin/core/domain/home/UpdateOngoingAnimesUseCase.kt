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

package ru.vladsaybulin.core.domain.home

import ru.vladsaybulin.core.domain.repository.AnimeRepository
import ru.vladsaybulin.core.domain.repository.LastRequestRepository
import ru.vladsaybulin.model.request.Request
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class UpdateOngoingAnimesUseCase @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val lastRequestRepository: LastRequestRepository,
) {
    suspend operator fun invoke(
        forceRefresh: Boolean,
        limit: Int = DefaultOngoingAnimesLimit
    ) {
        if (!forceRefresh && !isExpired()) return

        animeRepository.refreshOngoingAnimes(limit)
        lastRequestRepository.updateLastRequest(Request.ANIME_ONGOINGS, 0)
    }

    private suspend fun isExpired() = lastRequestRepository.isRequestExpired(
        request = Request.ANIME_ONGOINGS,
        targetId = 0,
        ttl = OngoingAnimesRequestTTL
    )
}

private val OngoingAnimesRequestTTL = 24.hours
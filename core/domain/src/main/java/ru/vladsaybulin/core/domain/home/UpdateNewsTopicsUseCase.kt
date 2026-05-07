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

import ru.vladsaybulin.core.domain.repository.LastRequestRepository
import ru.vladsaybulin.core.domain.repository.TopicsRepository
import ru.vladsaybulin.model.request.Request
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class UpdateNewsTopicsUseCase @Inject constructor(
    private val topicsRepository: TopicsRepository,
    private val lastRequestRepository: LastRequestRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = true) {
        if (!forceRefresh && !isExpired()) return

        topicsRepository.refreshNewsTopics()
        lastRequestRepository.updateLastRequest(Request.NEWS, 0)
    }

    private suspend fun isExpired() = lastRequestRepository.isRequestExpired(
        request = Request.NEWS,
        targetId = 0,
        ttl = NewsTTL
    )

}

private val NewsTTL = 2.hours
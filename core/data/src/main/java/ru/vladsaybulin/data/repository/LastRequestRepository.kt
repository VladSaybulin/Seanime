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

package ru.vladsaybulin.data.repository

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.vladsaybulin.core.domain.repository.LastRequestRepository as DomainLastRequestRepository
import ru.vladsaybulin.database.dao.LastRequestDao
import ru.vladsaybulin.database.models.lastrequest.LastRequestEntity
import ru.vladsaybulin.model.request.Request
import javax.inject.Inject
import kotlin.time.Duration

class LastRequestRepository @Inject constructor(private val lastRequestDao: LastRequestDao) : DomainLastRequestRepository {

    override suspend fun isRequestExpired(request: Request, targetId: Long, ttl: Duration): Boolean {
        val lastRequestDate = lastRequestDao.getLastRequestDate(request, targetId) ?: return true
        return (lastRequestDate + ttl) < Clock.System.now()
    }

    override suspend fun updateLastRequest(
        request: Request,
        targetId: Long,
        requestDate: Instant
    ) {
        lastRequestDao.insertOrReplaceLastRequestDate(
            LastRequestEntity(
                request = request,
                targetId = targetId,
                requestDate = requestDate
            )
        )
    }

}
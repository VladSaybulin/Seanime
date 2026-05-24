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

package ru.vladsaybulin.data.request

import kotlinx.datetime.Clock
import ru.vladsaybulin.database.DatabaseTransactionRunner
import ru.vladsaybulin.database.dao.LastRequestDao
import ru.vladsaybulin.database.models.lastrequest.LastRequestEntity
import javax.inject.Inject

class RequestSyncer @Inject constructor(
    private val lastRequestDao: LastRequestDao,
    private val transaction: DatabaseTransactionRunner,
    private val clock: Clock
) {
    suspend fun sync(
        key: RequestKey.Cached,
        forceRefresh: Boolean,
        strategy: TTLStrategy,
        block: suspend UpdateScope.() -> Unit
    ) {
        if (forceRefresh || shouldRefresh(key, strategy)) {
            UpdateScopeImpl(key).block()
        }
    }

    private suspend fun shouldRefresh(key: RequestKey.Cached, strategy: TTLStrategy): Boolean {
        val lastRequest = lastRequestDao.getLastRequestDate(key.type, key.targetId)
        return lastRequest == null || strategy.isExpired(clock.now(), lastRequest)
    }

    private inner class UpdateScopeImpl(private val key: RequestKey.Cached) : UpdateScope {
        override suspend fun write(block: suspend () -> Unit) = transaction {
            block()
            lastRequestDao.insertOrReplaceLastRequestDate(
                LastRequestEntity(
                    type = key.type,
                    targetId = key.targetId,
                    date = clock.now()
                )
            )
        }
    }
}
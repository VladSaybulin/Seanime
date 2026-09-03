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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers
import javax.inject.Inject

/**
 * Entry point for request synchronization.
 *
 * Combines two concerns:
 * - deduplication of in-flight calls ([RequestDeduplicator]),
 * - TTL-based cache synchronization ([RequestSyncer]).
 */
class RequestCoordinator @Inject constructor(
    private val deduplicator: RequestDeduplicator,
    private val syncer: RequestSyncer,
    @param:Dispatcher(ShikiDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * Executes [block] on IO and deduplicates it by [key].
     */
    suspend fun <T> request(
        key: RequestKey,
        block: suspend () -> T
    ) = withContext(ioDispatcher) {
        deduplicator.request(key, block)
    }

    /**
     * Performs cache-aware synchronization for a cached [key].
     *
     * The refresh runs only when [ttlStrategy] says cached data expired.
     * To force refresh use [ru.vladsaybulin.data.TTLStrategies.ForceRefresh]
     *
     * @return the result of [block], or `null` if refresh was not required.
     */
    suspend fun <T> sync(
        key: RequestKey.Cached,
        ttlStrategy: TTLStrategy,
        block: suspend UpdateScope.() -> T
    ): T? {
        return request(key) {
            syncer.sync(key, ttlStrategy, block)
        }
    }
}
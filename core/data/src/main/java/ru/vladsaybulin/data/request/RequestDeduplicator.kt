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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import ru.vladsaybulin.common.network.di.ApplicationScope
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

/**
 * Deduplicates concurrent requests with the same [RequestKey].
 *
 * If multiple callers request the same key at the same time, they all await a single
 * in-flight coroutine instead of starting duplicate work.
 */
class RequestDeduplicator @Inject constructor(
    @param:ApplicationScope private val coroutineScope: CoroutineScope
) {
    private val deferredRequests = ConcurrentHashMap<RequestKey, Deferred<*>>()

    /**
     * Runs [block] once per [key] while it is in-flight and shares its result with all callers.
     */
    suspend fun <T> request(key: RequestKey, block: suspend () -> T): T {
        val deferred = deferredRequests.computeIfAbsent(key) {
            coroutineScope.async { block() }.removeOnCompletion(key)
        }

        @Suppress("UNCHECKED_CAST")
        return deferred.await() as T
    }

    private fun Deferred<*>.removeOnCompletion(key: RequestKey): Deferred<*> {
        invokeOnCompletion { deferredRequests.remove(key, this) }
        return this
    }
}
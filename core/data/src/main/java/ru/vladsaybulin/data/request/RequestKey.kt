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

import ru.vladsaybulin.database.models.lastrequest.RequestType

/**
 * Stable key used by synchronization infrastructure.
 *
 * The key identifies a logical request so the system can:
 * - deduplicate concurrent calls,
 * - read and write last successful refresh timestamps.
 */
sealed interface RequestKey {
    /**
     * Key for requests persisted in `last_requests`.
     *
     * [type] identifies the data group, [targetId] scopes the request to a concrete entity
     * when needed (for example title id, character id, etc).
     */
    data class Cached(val type: RequestType, val targetId: Long = 0L) : RequestKey
}

/**
 * Helper for building [RequestKey.Cached] keys.
 */
fun cachedKey(type: RequestType, targetId: Long = 0L): RequestKey.Cached = RequestKey.Cached(type, targetId)
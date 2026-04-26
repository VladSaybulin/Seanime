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

package ru.vladsaybulin.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

suspend fun sync (
    ttl: Duration,
    lastRequestDateFlow: Flow<Instant>,
    updateLastRequest: suspend (Instant) -> Unit,
    refresh: suspend () -> Unit,
) {
    val now = Clock.System.now()
    val lastRequest = lastRequestDateFlow.firstOrNull() ?: Instant.DISTANT_PAST
    if (now - lastRequest < ttl) return
    refresh()
    updateLastRequest(now)
}

suspend fun sync(
    ttl: Duration,
    readLastUpdateDate: suspend () -> Instant?,
    refresh: suspend () -> Unit,
) {
    val now = Clock.System.now()
    val lastRequest = readLastUpdateDate() ?: Instant.DISTANT_PAST
    if (now - lastRequest < ttl) return
    refresh()
}

suspend fun <T> sync (
    param: T,
    ttl: Duration,
    readLastUpdateDate: suspend (T) -> Instant?,
    refresh: suspend (T) -> Unit,
) {
    val now = Clock.System.now()
    val lastRequest = readLastUpdateDate(param) ?: Instant.DISTANT_PAST
    if (now - lastRequest < ttl) return
    refresh(param)
}
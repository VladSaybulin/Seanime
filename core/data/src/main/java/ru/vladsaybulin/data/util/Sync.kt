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
    updateLastRequest: suspend (Instant) -> Unit,
    refresh: suspend () -> Unit,
) {
    val now = Clock.System.now()
    val lastRequest = readLastUpdateDate() ?: Instant.DISTANT_PAST
    if (now - lastRequest < ttl) return
    refresh()
    updateLastRequest(now)
}
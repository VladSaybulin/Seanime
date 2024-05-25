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
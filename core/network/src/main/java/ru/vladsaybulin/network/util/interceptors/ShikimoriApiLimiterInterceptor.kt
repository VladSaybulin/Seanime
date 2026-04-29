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

package ru.vladsaybulin.network.util.interceptors

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Global requests limiter to Shikimori API for OkHttpClient.
 *
 * Observes local [REQUESTS_PER_SECOND] and [REQUESTS_PER_MINUTE] limits,
 * and also takes into account server cooldown after HTTP 429 via the Retry-After header.
 */
@Singleton
class ShikimoriApiLimiterInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            awaitPermit()
            val response = chain.proceed(chain.request())
            if (response.code == HTTP_TOO_MANY_REQUESTS) {
                updateCooldownFromRetryAfter(response)
            }
            response
        }
    }

    private val lock = Any()

    @VisibleForTesting
    internal val secondWindowTimestamps = ArrayDeque<Long>()

    @VisibleForTesting
    internal val minuteWindowTimestamps = ArrayDeque<Long>()

    @VisibleForTesting
    internal var serverCooldownUntilNanos = 0L

    private suspend fun awaitPermit() {
        while (true) {
            val waitNanos = synchronized(lock) {
                val now = System.nanoTime()

                trimWindow(secondWindowTimestamps, now - SECOND_WINDOW_NANOS)
                trimWindow(minuteWindowTimestamps, now - MINUTE_WINDOW_NANOS)

                val serverWait = (serverCooldownUntilNanos - now).coerceAtLeast(0L)

                if (secondWindowTimestamps.size < REQUESTS_PER_SECOND &&
                    minuteWindowTimestamps.size < REQUESTS_PER_MINUTE &&
                    serverWait == 0L
                ) {
                    secondWindowTimestamps.addLast(now)
                    minuteWindowTimestamps.addLast(now)
                    return
                }

                val secondWait = if (secondWindowTimestamps.size >= REQUESTS_PER_SECOND) {
                    secondWindowTimestamps.first() + SECOND_WINDOW_NANOS - now
                } else {
                    0L
                }

                val minuteWait = if (minuteWindowTimestamps.size >= REQUESTS_PER_MINUTE) {
                    minuteWindowTimestamps.first() + MINUTE_WINDOW_NANOS - now
                } else {
                    0L
                }

                maxOf(secondWait, minuteWait, serverWait)
            }

            if (waitNanos <= 0L) continue

            // Ceil nanos to millis for delay precision and to avoid active spinning.
            val waitMillis = (waitNanos + NANOS_IN_MILLISECOND - 1L) / NANOS_IN_MILLISECOND
            delay(waitMillis)
        }
    }

    private fun updateCooldownFromRetryAfter(response: Response) {
        val retryAfterHeader = response.header(RETRY_AFTER_HEADER) ?: return
        val retryAfterNanos = parseRetryAfterToNanos(retryAfterHeader) ?: return

        val boundedRetryAfterNanos = retryAfterNanos.coerceAtMost(MAX_RETRY_AFTER_NANOS)
        synchronized(lock) {
            val newCooldown = System.nanoTime() + boundedRetryAfterNanos
            if (newCooldown > serverCooldownUntilNanos) {
                serverCooldownUntilNanos = newCooldown
            }
        }
    }

    private fun parseRetryAfterToNanos(headerValue: String): Long? {
        val seconds = headerValue.toLongOrNull()
        if (seconds != null) {
            if (seconds <= 0L) return null
            return seconds * SECOND_WINDOW_NANOS
        }

        // RFC 7231: Retry-After может приходить как HTTP-date.
        return runCatching {
            val retryAtMillis = ZonedDateTime.parse(headerValue, DateTimeFormatter.RFC_1123_DATE_TIME)
                .toInstant()
                .toEpochMilli()
            val delayMillis = retryAtMillis - System.currentTimeMillis()
            if (delayMillis > 0L) delayMillis * NANOS_IN_MILLISECOND else 0L
        }.getOrNull()
    }

    private fun trimWindow(window: ArrayDeque<Long>, threshold: Long) {
        while (window.isNotEmpty() && window.first() <= threshold) {
            window.removeFirst()
        }
    }
}

/** The maximum number of requests in sliding window 1 second */
private const val REQUESTS_PER_SECOND = 5
/** The maximum number of requests in sliding window 1 minute */
private const val REQUESTS_PER_MINUTE = 90

private const val SECOND_WINDOW_NANOS = 1_000_000_000L
private const val MINUTE_WINDOW_NANOS = 60 * SECOND_WINDOW_NANOS
private const val NANOS_IN_MILLISECOND = 1_000_000L
private const val HTTP_TOO_MANY_REQUESTS = 429
private const val RETRY_AFTER_HEADER = "Retry-After"
private const val MAX_RETRY_AFTER_NANOS = 5L * 60L * SECOND_WINDOW_NANOS

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

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

/**
 * Unit tests for [ShikimoriApiLimiterInterceptor].
 */
class ShikimoriApiLimiterInterceptorTest {

    private lateinit var server: MockWebServer
    private lateinit var client: OkHttpClient
    private lateinit var interceptor: ShikimoriApiLimiterInterceptor

    // Константы лимитов (копирует private-константы из лимитера)
    private val requestsPerSecond = 5
    private val requestsPerMinute = 90
    private val secondWindowNanos = 1_000_000_000L
    private val minuteWindowNanos = 60 * secondWindowNanos

    @Before
    fun setUp() {
        interceptor = ShikimoriApiLimiterInterceptor()
        server = MockWebServer()
        server.start()
        client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .callTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    // region: Базовые сценарии

    @Test
    fun `first 5 requests pass without throttling`() {
        repeat(requestsPerSecond) { server.enqueue(MockResponse().setResponseCode(200)) }

        val startMs = System.currentTimeMillis()
        repeat(requestsPerSecond) { request() }
        val elapsedMs = System.currentTimeMillis() - startMs

        assertTrue(
            "Первые $requestsPerSecond запросов не должны тормозить (elapsed: ${elapsedMs}ms)",
            elapsedMs < 700
        )
    }

    @Test
    fun `6th request in one second window is throttled`() {
        repeat(requestsPerSecond + 1) { server.enqueue(MockResponse().setResponseCode(200)) }

        val startMs = System.currentTimeMillis()
        repeat(requestsPerSecond + 1) { request() }
        val elapsedMs = System.currentTimeMillis() - startMs

        assertTrue(
            "6-й запрос должен быть задержан (elapsed: ${elapsedMs}ms)",
            elapsedMs >= 900
        )
    }

    @Test
    fun `each request returns correct response code`() {
        server.enqueue(MockResponse().setResponseCode(200))
        val response = request()
        assertEquals(200, response.code)
        response.close()
    }

    // endregion

    // region: Per-minute лимит

    @Test
    fun `requests throttled when minute window is full`() {
        // Заполняем минутное окно 90 таймстемпами, которые истекут через ~100ms.
        // Это позволяет тесту завершиться быстро (ждать не 1 минуту, а ~100ms).
        val expireSoonNanos = System.nanoTime() - minuteWindowNanos + 100_000_000L // -59.9s
        repeat(requestsPerMinute) { interceptor.minuteWindowTimestamps.addLast(expireSoonNanos) }

        server.enqueue(MockResponse().setResponseCode(200))

        val startMs = System.currentTimeMillis()
        request()
        val elapsedMs = System.currentTimeMillis() - startMs

        assertTrue(
            "При полном минутном окне запрос должен подождать (elapsed: ${elapsedMs}ms)",
            elapsedMs >= 80
        )
    }

    @Test
    fun `minute window does not throttle when timestamps are expired`() {
        // Заполняем 90 таймстемпами давно истёкшими (2 минуты назад).
        val expiredNanos = System.nanoTime() - minuteWindowNanos - 1_000_000_000L
        repeat(requestsPerMinute) { interceptor.minuteWindowTimestamps.addLast(expiredNanos) }

        server.enqueue(MockResponse().setResponseCode(200))

        val startMs = System.currentTimeMillis()
        request()
        val elapsedMs = System.currentTimeMillis() - startMs

        assertTrue(
            "Истёкшие таймстемпы не должны блокировать запрос (elapsed: ${elapsedMs}ms)",
            elapsedMs < 500
        )
    }

    // endregion

    // region: 429 Retry-After

    @Test
    fun `429 with Retry-After seconds delays subsequent request`() {
        server.enqueue(MockResponse().setResponseCode(429).addHeader("Retry-After", "2"))
        server.enqueue(MockResponse().setResponseCode(200))

        val startMs = System.currentTimeMillis()
        request().close()
        request().close()
        val elapsedMs = System.currentTimeMillis() - startMs

        assertTrue(
            "После 429 с Retry-After: 2s задержка должна быть >= 2000ms (elapsed: ${elapsedMs}ms)",
            elapsedMs >= 1900
        )
    }

    @Test
    fun `429 with Retry-After http-date format sets server cooldown`() {
        // RFC_1123_DATE_TIME требует явно зоны "GMT"
        val futureDate = ZonedDateTime.now(ZoneId.of("GMT")).plusSeconds(30)
            .format(DateTimeFormatter.RFC_1123_DATE_TIME)

        server.enqueue(MockResponse().setResponseCode(429).addHeader("Retry-After", futureDate))
        request().close()

        assertTrue(
            "Cooldown должен быть выставлен после Retry-After HTTP-date",
            interceptor.serverCooldownUntilNanos > System.nanoTime()
        )
    }

    @Test
    fun `429 without Retry-After header does not crash and does not throttle`() {
        server.enqueue(MockResponse().setResponseCode(429))
        server.enqueue(MockResponse().setResponseCode(200))

        val startMs = System.currentTimeMillis()
        request().close()
        request().close()
        val elapsedMs = System.currentTimeMillis() - startMs

        assertTrue(
            "Без Retry-After не должно быть лишнего cooldown (elapsed: ${elapsedMs}ms)",
            elapsedMs < 1500
        )
    }

    @Test
    fun `429 with invalid Retry-After value does not crash`() {
        server.enqueue(MockResponse().setResponseCode(429).addHeader("Retry-After", "invalid"))
        server.enqueue(MockResponse().setResponseCode(200))
        // Не должно бросить исключение
        request().close()
        request().close()
    }

    @Test
    fun `429 with negative Retry-After seconds does not set cooldown`() {
        server.enqueue(MockResponse().setResponseCode(429).addHeader("Retry-After", "-5"))
        server.enqueue(MockResponse().setResponseCode(200))

        val startMs = System.currentTimeMillis()
        request().close()
        request().close()
        val elapsedMs = System.currentTimeMillis() - startMs

        assertTrue(
            "Отрицательный Retry-After не должен добавлять задержку (elapsed: ${elapsedMs}ms)",
            elapsedMs < 1500
        )
    }

    @Test
    fun `retry-after cooldown respects max cap of 5 minutes`() {
        // Сервер требует ждать 10 минут — должно быть ограничено до 5 минут.
        server.enqueue(MockResponse().setResponseCode(429).addHeader("Retry-After", "600"))
        request().close()

        val maxCapNanos = 5L * 60L * secondWindowNanos
        assertTrue(
            "Cooldown не должен превышать 5 минут",
            interceptor.serverCooldownUntilNanos <= System.nanoTime() + maxCapNanos + 100_000_000L
        )
    }

    // endregion

    // region: Параллельные запросы

    @Test
    fun `concurrent requests respect per-second limit`() {
        val totalRequests = requestsPerSecond + 1
        repeat(totalRequests) { server.enqueue(MockResponse().setResponseCode(200)) }

        val threads = List(totalRequests) {
            Thread { request().close() }
        }

        val startMs = System.currentTimeMillis()
        threads.forEach { it.start() }
        threads.forEach { it.join(10_000) }
        val elapsedMs = System.currentTimeMillis() - startMs

        // Один из (n+1) запросов должен ждать — итого задержка >= 900ms
        assertTrue(
            "При ${totalRequests} параллельных запросах должна быть задержка (elapsed: ${elapsedMs}ms)",
            elapsedMs >= 900
        )
        // Все запросы дошли до сервера
        assertEquals(totalRequests, server.requestCount)
    }

    // endregion

    // region: Helpers

    private fun request() = client
        .newCall(Request.Builder().url(server.url("/test")).build())
        .execute()

    // endregion
}

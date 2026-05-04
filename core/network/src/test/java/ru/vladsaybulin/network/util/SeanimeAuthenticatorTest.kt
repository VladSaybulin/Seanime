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

package ru.vladsaybulin.network.util

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.verifyBlocking
import ru.vladsaybulin.network.TokenProvider
import java.util.concurrent.TimeUnit

class SeanimeAuthenticatorTest {

    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `authenticator should not call TokenProvider when token is valid`() {
        val tokenProvider = mock<TokenProvider>()
        val requests = testAuthenticator(
            tokenProvider = tokenProvider,
            responses = listOf(
                MockResponse().setResponseCode(200)
            )
        )

        verifyNoInteractions(tokenProvider)

        val authHeader = requests.single().headers["Authorization"]
        assertEquals("Bearer token 1", authHeader)
    }

    @Test
    fun `authenticator must call getToken() when server returns code 401 and token provider issues another token`() {
        val tokenProvider = mock<TokenProvider> {
            onBlocking { getAccessToken() } doReturn "token 2"
        }

        val requests = testAuthenticator(
            tokenProvider = tokenProvider,
            responses = listOf(
                MockResponse().setResponseCode(401),
                MockResponse().setResponseCode(200)
            )
        )

        verifyBlocking(tokenProvider, times(1)) { getAccessToken() }

        assertEquals("Bearer token 1", requests[0].headers["Authorization"])
        assertEquals("Bearer token 2", requests[1].headers["Authorization"])
    }

    @Test
    fun `authenticator must call invalidate() when server returns code 401 and token provider issues same token`() {
        val tokenProvider = mock<TokenProvider> {
            onBlocking { getAccessToken() } doReturn "token 1"
        }

        val requests = testAuthenticator(
            tokenProvider = tokenProvider,
            responses = listOf(
                MockResponse().setResponseCode(401)
            )
        )
        verifyBlocking(tokenProvider, times(1)) { getAccessToken() }
        verifyBlocking(tokenProvider, times(1)) { logout() }

        val authHeader = requests.single().headers["Authorization"]
        assertEquals("Bearer token 1", authHeader)
    }

    fun testAuthenticator(
        tokenProvider: TokenProvider,
        responses: List<MockResponse>
    ): List<RecordedRequest> {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer token 1")
                    .build()
                chain.proceed(request)
            }
            .authenticator(SeanimeAuthenticator(tokenProvider))
            .build()

        responses.forEach { server.enqueue(it) }

        okHttpClient.newCall(
            Request.Builder()
                .url(server.url("/"))
                .build()
        ).execute().use { }

        return server.takeExactlyRequests(responses.size)
    }

    private fun MockWebServer.takeExactlyRequests(expectedCount: Int): List<RecordedRequest> {
        val requests = buildList(expectedCount) {
            repeat(expectedCount) { index ->
                val request = takeRequest(2, TimeUnit.SECONDS)
                assertNotNull("Request #${index + 1} was not received", request)
                add(request!!)
            }
        }

        val unexpectedRequest = takeRequest(200, TimeUnit.MILLISECONDS)
        assertNull("Expected exactly $expectedCount requests, but got more", unexpectedRequest)
        assertEquals(expectedCount, requestCount)

        return requests
    }
}
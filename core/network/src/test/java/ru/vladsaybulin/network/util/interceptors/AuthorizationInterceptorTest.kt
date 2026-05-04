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
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import ru.vladsaybulin.network.TokenProvider
import org.junit.Assert.*
import org.junit.Test

class AuthorizationInterceptorTest {

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
    fun `adds Authorization header when token is present`() {
        val tokenProvider = object : TokenProvider {
            override suspend fun getAccessToken(): String? = "token"
            override suspend fun logout() {}

        }

        val request = testInterceptor(tokenProvider)
        assertEquals("Bearer token", request.headers["Authorization"])
    }

    @Test
    fun `does not add Authorization header  when token is null`() {
        val tokenProvider = object : TokenProvider {
            override suspend fun getAccessToken(): String? = null
            override suspend fun logout() {}

        }

        val request = testInterceptor(tokenProvider)
        assertNull(request.headers["Authorization"])
    }

    fun testInterceptor(tokenProvider: TokenProvider): RecordedRequest {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(tokenProvider))
            .build()

        server.enqueue(MockResponse().apply { setResponseCode(200) })

        okHttpClient.newCall(
            Request.Builder()
                .url(server.url("/"))
                .build()
        ).execute()

        return server.takeRequest()
    }
}
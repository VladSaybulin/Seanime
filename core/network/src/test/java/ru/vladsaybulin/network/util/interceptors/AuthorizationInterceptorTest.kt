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

import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.vladsaybulin.core.auth.SessionManager
import java.util.concurrent.TimeUnit

class AuthorizationInterceptorTest {

    @Test
    fun `public request is passed through without token refresh`() {
        val sessionManager = mock<SessionManager>()
        val interceptor = AuthorizationInterceptor(sessionManager)
        val request = Request.Builder()
            .url("https://example.com/api/public")
            .build()
        val chain = FakeChain(request)

        interceptor.intercept(chain)

        assertSame(request, chain.proceededRequest)
    }

    @Test
    fun `authorized request adds bearer token and removes marker header`() {
        val sessionManager = mock<SessionManager>()
        runBlocking {
            whenever(sessionManager.getFreshToken()).thenReturn("fresh-token")
        }

        val interceptor = AuthorizationInterceptor(sessionManager)
        val request = Request.Builder()
            .url("https://example.com/api/private")
            .addHeader("X-Authorized-Call", "true")
            .build()
        val chain = FakeChain(request)

        interceptor.intercept(chain)

        val proceededRequest = requireNotNull(chain.proceededRequest)
        assertEquals("Bearer fresh-token", proceededRequest.header("Authorization"))
        assertNull(proceededRequest.header("X-Authorized-Call"))
    }

    @Test
    fun `authorized request without fresh token proceeds as is`() {
        val sessionManager = mock<SessionManager>()
        runBlocking {
            whenever(sessionManager.getFreshToken()).thenReturn(null)
        }

        val interceptor = AuthorizationInterceptor(sessionManager)
        val request = Request.Builder()
            .url("https://example.com/api/private")
            .addHeader("X-Authorized-Call", "true")
            .build()
        val chain = FakeChain(request)

        interceptor.intercept(chain)

        val proceededRequest = requireNotNull(chain.proceededRequest)
        assertEquals("true", proceededRequest.header("X-Authorized-Call"))
        assertTrue(proceededRequest.header("Authorization").isNullOrBlank())
    }

    private class FakeChain(private val sourceRequest: Request) : Interceptor.Chain {
        var proceededRequest: Request? = null

        override fun request(): Request = sourceRequest

        override fun proceed(request: Request): Response {
            proceededRequest = request
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build()
        }

        override fun connection(): Connection? = null

        override fun call(): Call = mock()

        override fun connectTimeoutMillis(): Int = 10_000

        override fun withConnectTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this

        override fun readTimeoutMillis(): Int = 10_000

        override fun withReadTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this

        override fun writeTimeoutMillis(): Int = 10_000

        override fun withWriteTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain = this
    }
}



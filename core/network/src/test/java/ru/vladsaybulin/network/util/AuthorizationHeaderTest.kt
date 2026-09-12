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

import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthorizationHeaderTest {

    @Test
    fun `request without authorized marker is treated as public`() {
        val request = Request.Builder()
            .url("https://example.com/api/test")
            .build()

        assertTrue(request.isPublicCall())
    }

    @Test
    fun `request with authorized marker is not treated as public`() {
        val request = Request.Builder()
            .url("https://example.com/api/test")
            .addHeader("X-Authorized-Call", "true")
            .build()

        assertFalse(request.isPublicCall())
    }

    @Test
    fun `removeAuthorizedCall removes marker header`() {
        val request = Request.Builder()
            .url("https://example.com/api/test")
            .addHeader("X-Authorized-Call", "true")
            .removeAuthorizedCall()
            .build()

        assertNull(request.header("X-Authorized-Call"))
        assertTrue(request.isPublicCall())
    }

    @Test
    fun `addBearerToken and getBearerToken roundtrip`() {
        val request = Request.Builder()
            .url("https://example.com/api/test")
            .addBearerToken("token-123")
            .build()

        assertEquals("token-123", request.getBearerToken())
    }

    @Test
    fun `replaceBearerToken rewrites existing authorization header`() {
        val request = Request.Builder()
            .url("https://example.com/api/test")
            .addBearerToken("old-token")
            .replaceBearerToken("new-token")
            .build()

        assertEquals("Bearer new-token", request.header("Authorization"))
        assertEquals("new-token", request.getBearerToken())
    }

    @Test
    fun `getBearerToken returns null for non bearer authorization header`() {
        val request = Request.Builder()
            .url("https://example.com/api/test")
            .addHeader("Authorization", "Basic abc")
            .build()

        assertNull(request.getBearerToken())
    }
}


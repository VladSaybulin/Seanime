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

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.vladsaybulin.core.auth.SessionManager
import javax.inject.Inject

/**
 * Handles 401 responses by attempting a token refresh via [SessionManager].
 *
 * - If a fresh token differs from the one that caused the 401 → retry with new token.
 * - If tokens are the same → the refresh also failed; trigger logout via [SessionManager].
 * - Skips the auth endpoint to avoid retry loops.
 */
class SeanimeAuthenticator @Inject constructor(
    private val sessionManager: SessionManager
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        if (response.code != 401) return@runBlocking null

        // Don't retry auth endpoint itself
        if (response.request.url.pathSegments.contains("oauth")) return@runBlocking null

        val staleToken = response.request.getBearerToken()
        val freshToken = sessionManager.getFreshToken() ?: run {
            sessionManager.logout()
            return@runBlocking null
        }

        // If token didn't change the refresh failed → logout
        if (freshToken == staleToken) {
            sessionManager.logout()
            return@runBlocking null
        }

        response.request.newBuilder()
            .replaceBearerToken(freshToken)
            .build()
    }
}

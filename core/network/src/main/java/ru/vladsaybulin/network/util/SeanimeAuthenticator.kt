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
import ru.vladsaybulin.network.TokenProvider
import javax.inject.Inject

/**
 * OkHttp [Authenticator] that handles `401 Unauthorized` responses.
 *
 * Behavior:
 * - if response code is not `401`, no retry is requested;
 * - if [TokenProvider] already has a newer token, the request is rebuilt with it;
 * - if the token from the failed request equals the current token,
 *   [TokenProvider.logout] is called and retry is canceled.
 *
 * @property tokenProvider Source and manager of access tokens.
 */
class SeanimeAuthenticator @Inject constructor(private val tokenProvider: TokenProvider) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null

        val tokenFromRequest = response.request.getAuthorizationBearer()
        val actualToken = runBlocking { tokenProvider.getAccessToken() } ?: return null

        if (tokenFromRequest == actualToken) {
            runBlocking { tokenProvider.logout() }
            return null
        }

        return response.request.newBuilder()
            .setAuthorizationBearer(actualToken)
            .build()
    }
}
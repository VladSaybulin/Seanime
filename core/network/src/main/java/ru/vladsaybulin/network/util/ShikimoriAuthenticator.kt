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
import ru.vladsaybulin.common.auth.LogoutAction
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import javax.inject.Inject

class ShikimoriAuthenticator @Inject constructor(
    private val authorization: ShikimoriAuthorization,
    private val logoutAction: LogoutAction
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        if (response.code != 401) return@runBlocking null
        val freshAccessToken = authorization.getFreshAccessToken() ?: return@runBlocking null
        val tokenFromResponse = response.request.getAccessToken() ?: return@runBlocking null
        if (freshAccessToken == tokenFromResponse) {
            logoutAction.logout()
            return@runBlocking response.request.newBuilder()
                .removeAuthorizationHeader()
                .build()
        }

        return@runBlocking response.request.newBuilder()
            .replaceAuthorizationHeader(freshAccessToken)
            .build()
    }
}

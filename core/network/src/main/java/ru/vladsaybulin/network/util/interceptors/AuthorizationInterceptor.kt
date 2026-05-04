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
import okhttp3.Interceptor
import okhttp3.Response
import ru.vladsaybulin.network.TokenProvider
import ru.vladsaybulin.network.util.setAuthorizationBearer
import javax.inject.Inject

/**
 * OkHttp interceptor that adds `Authorization: Bearer <token>` to outgoing requests.
 *
 * If [TokenProvider.getAccessToken] returns `null`, the request is sent without
 * an Authorization header.
 *
 * @property tokenProvider Source of the current access token.
 */
class AuthorizationInterceptor @Inject constructor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenProvider.getAccessToken() }

        val request = if (token != null) {
            chain.request().newBuilder()
                .setAuthorizationBearer(token)
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
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
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.network.util.addAuthorizationHeader
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val authorization: ShikimoriAuthorization
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            val accessToken = authorization.getFreshAccessToken()
                ?: return@runBlocking chain.proceed(chain.request())
            val request = chain.request().newBuilder()
                .addAuthorizationHeader(accessToken)
                .build()
            chain.proceed(request)
        }
    }
}
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

package ru.vladsaybulin.network.datasource

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.POST
import ru.vladsaybulin.network.models.NetworkError
import ru.vladsaybulin.network.models.NetworkResponse
import ru.vladsaybulin.network.models.auth.OAuthTokenBody
import ru.vladsaybulin.network.models.auth.OAuthTokenRequest
import javax.inject.Inject

private interface OAuthApi {
    /**
     * Single endpoint; see docs/auth_backend_contract.md.
     * Path must match AUTH_PATH so AuthorizationInterceptor skips the Bearer header.
     */
    @POST("/oauth/token")
    suspend fun action(@Body request: OAuthTokenRequest): Response<OAuthTokenBody>
}

class AuthDataSource @Inject constructor(retrofit: Retrofit) {
    private val api = retrofit.create<OAuthApi>()

    suspend fun action(request: OAuthTokenRequest): NetworkResponse<OAuthTokenBody> {
        val response = api.action(request)
        return if (response.isSuccessful) {
            val body = response.body() ?: return NetworkResponse.Error(
                IllegalStateException("Response body is null for successful response")
            )
            NetworkResponse.Success(body)
        } else {
            NetworkResponse.Error(
                NetworkError(
                    code = response.code(),
                    description = response.message()
                )
            )
        }
    }
}
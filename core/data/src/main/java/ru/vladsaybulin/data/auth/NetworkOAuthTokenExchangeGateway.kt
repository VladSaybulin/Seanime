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

package ru.vladsaybulin.data.auth

import ru.vladsaybulin.core.auth.AuthException
import ru.vladsaybulin.core.auth.ShikimoriAuthInfo
import ru.vladsaybulin.core.auth.StoredTokens
import ru.vladsaybulin.core.auth.TokenExchangeGateway
import ru.vladsaybulin.network.datasource.AuthDataSource
import ru.vladsaybulin.network.models.NetworkError
import ru.vladsaybulin.network.models.NetworkResponse
import ru.vladsaybulin.network.models.auth.OAuthTokenBody
import ru.vladsaybulin.network.models.auth.OAuthTokenRequest
import javax.inject.Inject

private const val AUTHORIZATION_CODE = "authorization_code"
private const val REFRESH_TOKEN = "refresh_token"

class NetworkOAuthTokenExchangeGateway @Inject constructor(
    private val dataSource: AuthDataSource,
    private val authInfo: ShikimoriAuthInfo,
) : TokenExchangeGateway {

    override suspend fun exchange(code: String): StoredTokens =
        call(
            OAuthTokenRequest(
                grantType = AUTHORIZATION_CODE,
                code = code,
                clientId = authInfo.clientId,
                clientSecret = authInfo.clientSecret,
                redirectUri = authInfo.redirectUri
            )
        )

    override suspend fun refresh(refreshToken: String): StoredTokens =
        call(
            OAuthTokenRequest(
                grantType = REFRESH_TOKEN,
                refreshToken = refreshToken,
                clientId = authInfo.clientId,
                clientSecret = authInfo.clientSecret
            )
        )

    override suspend fun revoke(refreshToken: String) {
    }

    private suspend fun call(request: OAuthTokenRequest): StoredTokens {
        val response = dataSource.action(request)
        if (response is NetworkResponse.Success) {
            return checkNotNull(response.body).toStoredTokens()
        }

        when (val exception = (response as NetworkResponse.Error).exception) {
            is NetworkError -> {
                throw if (exception.code in 400..<500) {
                    AuthException.Unauthorized()
                } else {
                    IllegalStateException("OAuth backend error: ${exception.description}")
                }
            }

            else -> throw exception
        }
    }

    private fun OAuthTokenBody.toStoredTokens() = StoredTokens(
        accessToken = accessToken,
        refreshToken = refreshToken,
        expiresAtMs = (createdAt + expiresIn) * 1_000L // Convert to ms
    )
}






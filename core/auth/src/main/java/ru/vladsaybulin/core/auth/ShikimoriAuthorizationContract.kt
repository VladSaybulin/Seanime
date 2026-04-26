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

package ru.vladsaybulin.core.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import javax.inject.Inject

internal class ShikimoriAuthorizationContract @Inject constructor(
    private val authorizationConfig: AuthorizationServiceConfiguration,
    private val authInfo: ShikimoriAuthInfo,
) : ActivityResultContract<Unit, AuthorizationResult?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        val request = AuthorizationRequest.Builder(
            authorizationConfig,
            authInfo.clientId,
            ResponseTypeValues.CODE,
            Uri.parse(authInfo.redirectUri)
        )
            .setScope(authInfo.scope)
            .build()
        return AuthorizationService(context).getAuthorizationRequestIntent(request)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): AuthorizationResult? {
        if (intent == null) return null
        return AuthorizationResult(
            AuthorizationResponse.fromIntent(intent),
            AuthorizationException.fromIntent(intent)
        )
    }
}

internal data class AuthorizationResult(
    val response: AuthorizationResponse?,
    val exception: AuthorizationException?
)
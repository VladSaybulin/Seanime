package ru.vladsaybulin.core.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

typealias AuthorizationLauncher = ActivityResultLauncher<Unit>

fun ComponentActivity.registerAuthorizationLauncher(
    shikimoriAuthState: ShikimoriAuthState
): AuthorizationLauncher = registerForActivityResult(
    AuthorizationContract(
        checkNotNull(shikimoriAuthState.authState.authorizationServiceConfiguration)
    ),
    AuthorizationCallback(shikimoriAuthState)
)

fun AuthorizationLauncher.launch() = launch(Unit)

private data class AuthResult(
    val response: AuthorizationResponse?,
    val exception: AuthorizationException?
)

private class AuthorizationContract(
    private val authorizationConfig: AuthorizationServiceConfiguration,
) : ActivityResultContract<Unit, AuthResult?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        val request = AuthorizationRequest.Builder(
            authorizationConfig,
            BuildConfig.SHIKIMORI_CLIENT_ID,
            ResponseTypeValues.CODE,
            Uri.parse(BuildConfig.SHIKIMORI_AUTH_REDIRECT_URI)
        )
            .setScope("user_rates comments topics")
            .setAdditionalParameters(mapOf(UserAgent))
            .build()
        return AuthorizationService(context).getAuthorizationRequestIntent(request)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): AuthResult? {
        if (intent == null) return null
        return AuthResult(
            AuthorizationResponse.fromIntent(intent),
            AuthorizationException.fromIntent(intent)
        )
    }
}

private class AuthorizationCallback(
    private val shikimoriAuthState: ShikimoriAuthState
) : ActivityResultCallback<AuthResult?> {

    override fun onActivityResult(result: AuthResult?) {
        if (result == null) return

        if (result.response == null) {
            shikimoriAuthState.onAuthorizationFailed(checkNotNull(result.exception))
            return
        }

        val tokenExchangeRequest = result.response.createTokenExchangeRequest(
            mapOf(UserAgent, ClientSecret)
        )

        shikimoriAuthState.service.performTokenRequest(tokenExchangeRequest) { tokenResponse, tokenException ->
            if (tokenResponse == null) {
                shikimoriAuthState.onTokenExchangeFailed(requireNotNull(tokenException))
                return@performTokenRequest
            }

            shikimoriAuthState.onLogin(tokenResponse)
        }
    }
}



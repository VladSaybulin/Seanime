package ru.vladsaybulin.core.auth

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

fun ComponentActivity.createAuthorizationService() = AuthorizationService(this)

fun ComponentActivity.startAuthorizationActivity(
    activityResultLauncher: ActivityResultLauncher<Intent>,
    authService: AuthorizationService = createAuthorizationService(),
    authServiceConfig: AuthorizationServiceConfiguration = DefaultAuthServiceConfiguration,
) {
    val request = AuthorizationRequest.Builder(
        authServiceConfig,
        BuildConfig.SHIKIMORI_CLIENT_ID,
        ResponseTypeValues.CODE,
        Uri.parse(BuildConfig.SHIKIMORI_AUTH_REDIRECT_URI)
    )
        .setScope("user_rates comments topics")
        .build()

    val intent = authService.getAuthorizationRequestIntent(request)
    activityResultLauncher.launch(intent)
}

class AuthorizationActivityResultCallback(
    private val authState: ShikimoriAuthState,
    private val authService: AuthorizationService
) : ActivityResultCallback<ActivityResult> {
    override fun onActivityResult(result: ActivityResult) {
        val data = result.data ?: return
        val authResponse = AuthorizationResponse.fromIntent(data)
        val authException = AuthorizationException.fromIntent(data)

        if (authResponse == null) {
            authState.onAuthorizationFailed(requireNotNull(authException))
            return
        }

        val tokenExchangeRequest = authResponse.createTokenExchangeRequest(
            mapOf("client_secret" to BuildConfig.SHIKIMORI_CLIENT_SECRET)
        )
        authService.performTokenRequest(tokenExchangeRequest) { tokenResponse, tokenException ->
            if (tokenResponse == null) {
                authState.onTokenExchangeFailed(requireNotNull(tokenException))
                return@performTokenRequest
            }

            authState.onLogin(tokenResponse)
        }
    }
}

val DefaultAuthServiceConfiguration = AuthorizationServiceConfiguration(
    Uri.parse("${BuildConfig.BASE_URL}/oauth/authorize"),
    Uri.parse("${BuildConfig.BASE_URL}/oauth/token")
)

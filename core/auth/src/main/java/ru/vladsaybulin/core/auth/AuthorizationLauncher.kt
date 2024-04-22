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
    shikimoriAuthorization: ShikimoriAuthorization
): AuthorizationLauncher = registerForActivityResult(
    AuthorizationContract(ShikimoriAuthorizationServiceConfiguration),
    AuthorizationCallback(shikimoriAuthorization)
)

fun AuthorizationLauncher.launch() = launch(Unit)

private class AuthorizationContract(
    private val authorizationConfig: AuthorizationServiceConfiguration,
) : ActivityResultContract<Unit, AuthorizationResult?>() {
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

    override fun parseResult(resultCode: Int, intent: Intent?): AuthorizationResult? {
        if (intent == null) return null
        return AuthorizationResult(
            AuthorizationResponse.fromIntent(intent),
            AuthorizationException.fromIntent(intent)
        )
    }
}

private class AuthorizationCallback(
    private val shikimoriAuthorization: ShikimoriAuthorization
) : ActivityResultCallback<AuthorizationResult?> {

    override fun onActivityResult(result: AuthorizationResult?) {
        if (result == null) return
        shikimoriAuthorization.onAuthorizationResult(result)
    }
}



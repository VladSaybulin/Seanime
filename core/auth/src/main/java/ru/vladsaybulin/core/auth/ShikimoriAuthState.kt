package ru.vladsaybulin.core.auth

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.TokenResponse
import ru.vladsaybulin.common.network.di.ApplicationScope
import ru.vladsaybulin.datastore.ShikiPreferencesDataSource
import javax.inject.Inject

class ShikimoriAuthState @Inject internal constructor(
    private val preferencesDataSource: ShikiPreferencesDataSource,
    @ApplicationContext context: Context,
    @ApplicationScope private val appScope: CoroutineScope,
) {
    internal var authState: AuthState = runBlocking {
        preferencesDataSource.authStateJsonString.firstOrNull()
            ?.takeIf { it.isNotBlank() }
            ?.let { AuthState.jsonDeserialize(it) }
            ?: InitialAuthState
    }

    internal val service = AuthorizationService(context)

    val isAuthorized: Boolean
        get() = authState.isAuthorized

    val accessToken: String?
        get() = getFreshAccessToken()

    internal fun onLogin(response: TokenResponse) {
        authState.update(response, null)
        saveAuthState()
    }

    fun onLogout() {
        authState = InitialAuthState
        saveAuthState()
    }

    internal fun onAuthorizationFailed(exception: AuthorizationException) {
        exception.printStackTrace()
        authState.update(null as AuthorizationResponse?, exception)
        saveAuthState()
    }

    internal fun onTokenExchangeFailed(exception: AuthorizationException) {
        exception.printStackTrace()
        authState.update(null as TokenResponse?, exception)
        saveAuthState()
    }

    private fun getFreshAccessToken(): String? {
        var accessToken: String? = authState.accessToken
        authState.performActionWithFreshTokens(service) { freshAccessToken, _, ex ->
            if (ex == null) return@performActionWithFreshTokens
            if (accessToken != freshAccessToken) {
                saveAuthState()
                accessToken = freshAccessToken
            }
        }
        return accessToken
    }

    private fun saveAuthState() {
        appScope.launch {
            preferencesDataSource.setAuthStateJsonString(authState.jsonSerializeString())
        }
    }

    companion object {
        val InitialAuthState
            get() = AuthState(
            AuthorizationServiceConfiguration(
                Uri.parse("${BuildConfig.BASE_URL}/oauth/authorize"),
                Uri.parse("${BuildConfig.BASE_URL}/oauth/token")
            )
        )
    }
}
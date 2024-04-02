package ru.vladsaybulin.core.auth

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.TokenResponse
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.common.network.di.ApplicationScope
import ru.vladsaybulin.datastore.ShikiPreferencesDataSource
import javax.inject.Inject

interface ShikimoriAuthState {
    val isAuthorized: Boolean
    val accessToken: String?

    fun onLogin(response: TokenResponse)

    fun onLogout()

    fun onAuthorizationFailed(exception: AuthorizationException)

    fun onTokenExchangeFailed(exception: AuthorizationException)
}

class ShikimoriAuthStateImpl @Inject internal constructor(
    private val preferencesDataSource: ShikiPreferencesDataSource,
    @ApplicationContext context: Context,
    @ApplicationScope private val appScope: CoroutineScope,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : ShikimoriAuthState {
    private var authState: AuthState = AuthState()
    private val service = AuthorizationService(context)

    override val isAuthorized: Boolean
        get() = authState.isAuthorized

    override val accessToken: String?
        get() = getFreshAccessToken()

    init {
        readAuthState()
    }

    override fun onLogin(response: TokenResponse) {
        authState.update(response, null)
        saveAuthState()
    }

    override fun onLogout() {
        if (!isAuthorized) return
        authState = AuthState()
        saveAuthState()
    }

    override fun onAuthorizationFailed(exception: AuthorizationException) {
        exception.printStackTrace()
        authState.update(null as AuthorizationResponse?, exception)
        saveAuthState()
    }

    override fun onTokenExchangeFailed(exception: AuthorizationException) {
        exception.printStackTrace()
        authState.update(null as TokenResponse?, exception)
        saveAuthState()
    }

    private fun getFreshAccessToken(): String? {
        var accessToken: String? = null
        authState.performActionWithFreshTokens(service) { freshAccessToken, _, _ ->
            accessToken = freshAccessToken
        }
        return accessToken
    }

    private fun saveAuthState() {
        appScope.launch(ioDispatcher) {
            preferencesDataSource.setAuthStateJsonString(authState.jsonSerializeString())
        }
    }

    private fun readAuthState() {
        appScope.launch {
            val jsonStr = preferencesDataSource.authStateJsonString.firstOrNull()
            authState = if (jsonStr.isNullOrEmpty()) {
                AuthState(
                    AuthorizationServiceConfiguration(
                        Uri.parse("${BuildConfig.BASE_URL}/oauth/authorize"),
                        Uri.parse("${BuildConfig.BASE_URL}/oauth/token")
                    )
                )
            } else {
                AuthState.jsonDeserialize(jsonStr)
            }
        }
    }
}
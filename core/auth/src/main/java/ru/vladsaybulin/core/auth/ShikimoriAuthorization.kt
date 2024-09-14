package ru.vladsaybulin.core.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.common.network.di.ApplicationScope
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class ShikimoriAuthorization @Inject internal constructor(
    private val sharedPreferences: SharedPreferences,
    private val client: Lazy<ClientAuthentication>,
    private val service: Lazy<AuthorizationService>,
    @ApplicationScope private val appScope: CoroutineScope,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    private var appAuthState: AuthState? = null

    private val _shikimoriAuthState = MutableStateFlow(ShikimoriAuthState.LOGGED_OUT)
    val shikimoriAuthState = _shikimoriAuthState.asStateFlow()

    init {
        appScope.launch { readAuthState() }
    }

    suspend fun getFreshAccessToken(): String? = if (appAuthState != null) {
        suspendCoroutine { cont ->
            appAuthState?.performActionWithFreshTokens(service.get()) { freshAccessToken, _, exception ->
                if (exception != null) {
                    refreshTokenFailed(exception)
                    cont.resumeWithException(exception)
                } else {
                    cont.resume(freshAccessToken)
                }
            }
        }
    } else null

    internal fun logIn(authResponse: AuthorizationResponse) {
        val request = authResponse.createTokenExchangeRequest()
        service.get().performTokenRequest(request, client.get()) { tokenResponse, exception ->
            if (tokenResponse != null) {
                appAuthState = AuthState(authResponse, tokenResponse, null)
                onAuthStateUpdated()
            } else {
                refreshTokenFailed(checkNotNull(exception))
            }
        }
    }

    fun logOut() {
        appAuthState = AuthState()
        onAuthStateUpdated()
    }

    private fun onAuthStateUpdated(skipWrite: Boolean = false) {
        _shikimoriAuthState.value = if (appAuthState != null && appAuthState!!.isAuthorized) {
            ShikimoriAuthState.LOGGED_IN
        } else {
            ShikimoriAuthState.LOGGED_OUT
        }

        if (!skipWrite) {
            appScope.launch(ioDispatcher) {
                sharedPreferences.edit(commit = true) {
                    putString(AUTH_STATE_KEY, appAuthState?.jsonSerializeString())
                }
            }
        }
    }

    private suspend fun readAuthState() {
        appAuthState = withContext(ioDispatcher) {
            sharedPreferences.getString(AUTH_STATE_KEY, null)
                ?.ifBlank { null }
                ?.let { AuthState.jsonDeserialize(it) }
        }
        onAuthStateUpdated(skipWrite = true)
    }

    fun authorizationFailed(exception: AuthorizationException) {
        appAuthState = null
        onAuthStateUpdated()
        Log.e("ShikimoriAuth", "Authorization failed with exception: $exception")
    }

    private fun refreshTokenFailed(exception: AuthorizationException) {
        appAuthState = null
        onAuthStateUpdated()
        Log.e("ShikimoriAuth", "Refresh token failed with exception: $exception")
    }
}

private const val AUTH_STATE_KEY = "auth_state"

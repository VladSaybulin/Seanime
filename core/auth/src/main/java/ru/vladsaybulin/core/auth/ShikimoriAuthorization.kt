package ru.vladsaybulin.core.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService
import net.openid.appauth.ClientAuthentication
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.common.network.di.ApplicationScope
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShikimoriAuthorization @Inject internal constructor(
    private val sharedPreferences: SharedPreferences,
    private val client: Lazy<ClientAuthentication>,
    private val service: Lazy<AuthorizationService>,
    private val info: Lazy<ShikimoriAuthInfo>,
    @ApplicationScope private val appScope: CoroutineScope,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    private var appAuthState = AuthState()

    private val _shikimoriAuthState = MutableStateFlow(ShikimoriAuthState.LOGGED_OUT)
    val shikimoriAuthState = _shikimoriAuthState.asStateFlow()

    init {
        appScope.launch { readAuthState() }
    }

    fun getFreshAccessToken(): String? {
        if (!appAuthState.isAuthorized) return null
        if (appAuthState.needsTokenRefresh) {
            refreshToken()
        }
        return appAuthState.accessToken
    }

    suspend fun signOut() {
        val currentAccessToken = getFreshAccessToken()
        appAuthState = AuthState()
        onAuthStateUpdated()
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        val request = Request.Builder()
            .url("${BuildConfig.BASE_URL}/api/users/sign_out")
            .method("POST", "".toRequestBody())
            .addHeader("UserAgent", info.get().userAgent)
            .addHeader("Authorization", "Bearer $currentAccessToken")
            .build()
        withContext(ioDispatcher) {
            client.newCall(request).execute().body
        }
    }

    fun onNewAuthState(newAuthState: AuthState?) {
        appAuthState = newAuthState ?: AuthState()
        onAuthStateUpdated()
    }

    private fun onAuthStateUpdated(skipWrite: Boolean = false) {
        _shikimoriAuthState.value = when {
            appAuthState.isAuthorized -> ShikimoriAuthState.LOGGED_IN
            else -> ShikimoriAuthState.LOGGED_OUT
        }
        if (!skipWrite) {
            appScope.launch(ioDispatcher) {
                sharedPreferences.edit(commit = true) {
                    putString(AUTH_STATE_KEY, appAuthState.jsonSerializeString())
                }
            }
        }
    }

    private suspend fun readAuthState() {
        appAuthState = withContext(ioDispatcher) {
            sharedPreferences.getString(AUTH_STATE_KEY, null)
                ?.ifBlank { null }
                ?.let { AuthState.jsonDeserialize(it) }
                ?: AuthState()
        }
        onAuthStateUpdated(skipWrite = true)
    }

    private fun refreshToken() {
        val tokenRequest = appAuthState.createTokenRefreshRequest()
        service.get().performTokenRequest(tokenRequest, client.get()) { response, exception ->
            appAuthState.update(response, exception)
            onAuthStateUpdated()
        }
    }
}

private const val AUTH_STATE_KEY = "auth_state"

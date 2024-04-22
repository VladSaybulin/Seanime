package ru.vladsaybulin.core.auth

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers
import ru.vladsaybulin.common.network.di.ApplicationScope
import ru.vladsaybulin.datastore.ShikiPreferencesDataSource
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShikimoriAuthorization @Inject internal constructor(
    private val preferencesDataSource: ShikiPreferencesDataSource,
    @ApplicationContext context: Context,
    @ApplicationScope private val appScope: CoroutineScope,
    @Dispatcher(ShikiDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {
    private var appAuthState: AuthState = runBlocking {
        preferencesDataSource.authStateJsonString.firstOrNull()
            ?.takeIf { it.isNotBlank() }
            ?.let { AuthState.jsonDeserialize(it) }
            ?: EmptyAuthState
    }

    private val service = AuthorizationService(context)

    private val _authState = MutableStateFlow(getCurrentState())
    val authState = _authState.asStateFlow()

    val accessToken: String?
        get() = getFreshAccessToken()

    internal fun onAuthorizationResult(result: AuthorizationResult) {

        appAuthState.update(result.response, result.exception)
        if (result.response == null) {
            onAppAuthStateChanged()
            return
        }

        val tokenRequest = result.response.createTokenExchangeRequest(DefaultAdditionalParams)
        service.performTokenRequest(tokenRequest) { tokenResponse, tokenException ->
            appAuthState.update(tokenResponse, tokenException)
            onAppAuthStateChanged()
        }
    }

    fun signOut() {
        val currentAccessToken = accessToken
        appAuthState = EmptyAuthState
        onAppAuthStateChanged()
        appScope.launch(ioDispatcher) {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .build()
            val request = Request.Builder()
                .url("${BuildConfig.BASE_URL}/api/users/sign_out")
                .method("POST", "".toRequestBody())
                .addHeader(UserAgent.first, UserAgent.second)
                .addHeader("Authorization", "Bearer $currentAccessToken")
                .build()
            client.newCall(request).execute().body
        }
    }

    private fun getFreshAccessToken(): String? {
        var accessToken: String? = appAuthState.accessToken ?: return null

        appAuthState.performActionWithFreshTokens(
            service,
            DefaultAdditionalParams
        ) { freshAccessToken, _, _ ->
            if (accessToken != freshAccessToken) {
                accessToken = freshAccessToken
                onAppAuthStateChanged()
            }
        }
        return accessToken
    }

    private fun onAppAuthStateChanged() {
        _authState.value = getCurrentState()
        appScope.launch {
            preferencesDataSource.setAuthStateJsonString(appAuthState.jsonSerializeString())
        }
    }

    private fun getCurrentState(): ShikimoriAuthState = when {
        appAuthState.isAuthorized -> ShikimoriAuthState.Authorized
        appAuthState.authorizationException == null -> ShikimoriAuthState.NotAuthorized
        else -> ShikimoriAuthState.Error(appAuthState.authorizationException!!)
    }

    companion object {
        val EmptyAuthState: AuthState
            get() = AuthState(ShikimoriAuthorizationServiceConfiguration)
    }
}
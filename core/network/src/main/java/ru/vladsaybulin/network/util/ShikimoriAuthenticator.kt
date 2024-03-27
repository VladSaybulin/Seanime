package ru.vladsaybulin.network.util

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.vladsaybulin.core.auth.ShikimoriAuthState
import javax.inject.Inject

class ShikimoriAuthenticator @Inject constructor(
    private val authState: ShikimoriAuthState
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null
        val freshAccessToken = authState.accessToken ?: return null
        val tokenFromResponse = response.request.getAccessToken() ?: return null
        if (freshAccessToken == tokenFromResponse) {
            authState.onLogout()
            return response.request.newBuilder()
                .removeAuthorizationHeader()
                .build()
        }

        return response.request.newBuilder()
            .replaceAuthorizationHeader(freshAccessToken)
            .build()
    }
}
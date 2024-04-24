package ru.vladsaybulin.network.util

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import javax.inject.Inject

class ShikimoriAuthenticator @Inject constructor(
    private val authorization: ShikimoriAuthorization
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null
        val freshAccessToken = authorization.getFreshAccessToken() ?: return null
        val tokenFromResponse = response.request.getAccessToken() ?: return null
        if (freshAccessToken == tokenFromResponse) {
            runBlocking { authorization.signOut() }
            return response.request.newBuilder()
                .removeAuthorizationHeader()
                .build()
        }

        return response.request.newBuilder()
            .replaceAuthorizationHeader(freshAccessToken)
            .build()
    }
}
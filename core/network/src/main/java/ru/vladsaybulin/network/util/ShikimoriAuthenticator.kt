package ru.vladsaybulin.network.util

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import ru.vladsaybulin.common.auth.LogoutAction
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import javax.inject.Inject

class ShikimoriAuthenticator @Inject constructor(
    private val authorization: ShikimoriAuthorization,
    private val logoutAction: LogoutAction
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        if (response.code != 401) return@runBlocking null
        val freshAccessToken = authorization.getFreshAccessToken() ?: return@runBlocking null
        val tokenFromResponse = response.request.getAccessToken() ?: return@runBlocking null
        if (freshAccessToken == tokenFromResponse) {
            logoutAction.logout()
            return@runBlocking response.request.newBuilder()
                .removeAuthorizationHeader()
                .build()
        }

        return@runBlocking response.request.newBuilder()
            .replaceAuthorizationHeader(freshAccessToken)
            .build()
    }
}

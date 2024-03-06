package ru.vladsaybulin.network.common

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class ShikiAuthenticator @Inject constructor(private val tokensHolder: TokensHolder) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code != 401) return null
        return runBlocking { authenticate(response.request) }
    }

    private suspend fun authenticate(request: Request): Request? {

        val tokenFromRequest = request.getAccessToken() ?: return null
        val currentToken = tokensHolder.getAccessToken() ?: return null

        if (tokenFromRequest != currentToken) return null

        val newAccessToken = tokensHolder.refreshAccessToken() ?: return null

        return request.newBuilder()
            .addAuthorizationHeader(newAccessToken)
            .build()
    }
}
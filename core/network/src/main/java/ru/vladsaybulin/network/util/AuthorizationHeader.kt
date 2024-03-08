package ru.vladsaybulin.network.util

import okhttp3.Request

internal fun Request.Builder.addAuthorizationHeader(accessToken: String): Request.Builder =
    this.apply {
        addHeader(AUTHORIZATION_HEADER, "$AUTHORIZATION_TYPE $accessToken")
    }

internal fun Request.getAccessToken(): String? {
    val headerValue = header(AUTHORIZATION_HEADER) ?: return null
    if (!headerValue.startsWith(AUTHORIZATION_TYPE)) return null
    return headerValue.substring(AUTHORIZATION_TYPE.length + 1, headerValue.length)
}

private const val AUTHORIZATION_TYPE = "Bearer"
private const val AUTHORIZATION_HEADER = "Authorization"
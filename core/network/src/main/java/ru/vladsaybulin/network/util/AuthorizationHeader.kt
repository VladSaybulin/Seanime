package ru.vladsaybulin.network.util

import okhttp3.Request

fun Request.Builder.replaceAuthorizationHeader(accessToken: String): Request.Builder {
    removeAuthorizationHeader()
    addAuthorizationHeader(accessToken)
    return this
}

fun Request.Builder.removeAuthorizationHeader(): Request.Builder {
    removeHeader(AUTHORIZATION_HEADER_NAME)
    return this
}

fun Request.Builder.addAuthorizationHeader(accessToken: String): Request.Builder {
    addHeader(AUTHORIZATION_HEADER_NAME, "$AUTHORIZATION_TYPE_BEARER $accessToken")
    return this
}

fun Request.getAccessToken(): String? {
    val header = headers[AUTHORIZATION_HEADER_NAME] ?: return null
    if (!header.startsWith(AUTHORIZATION_TYPE_BEARER)) return null
    return header.substring(startIndex = AUTHORIZATION_TYPE_BEARER.length + 1)
}

private val AUTHORIZATION_HEADER_NAME = "Authorization"
private val AUTHORIZATION_TYPE_BEARER = "Bearer"
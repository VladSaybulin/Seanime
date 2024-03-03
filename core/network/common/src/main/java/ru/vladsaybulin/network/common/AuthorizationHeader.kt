package ru.vladsaybulin.network.common

import okhttp3.Request

internal fun Request.Builder.addAuthorizationHeader(accessToken: String): Request.Builder =
    this.apply {
        addHeader(AUTHORIZATION_HEADER, "Bearer: $accessToken")
    }

internal fun Request.getAccessToken(): String? {
    val headerValue = header(AUTHORIZATION_HEADER) ?: return null
    if (!headerValue.startsWith("Bearer: ")) return null
    return headerValue.substring(8, headerValue.length)
}

private const val AUTHORIZATION_HEADER = "Authorization"
package ru.vladsaybulin.core.auth

import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

internal class AuthorizationResult(
    val response: AuthorizationResponse?,
    val exception: AuthorizationException?
)
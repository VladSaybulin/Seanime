package ru.vladsaybulin.core.auth

import net.openid.appauth.AuthorizationException

sealed class ShikimoriAuthState {
    data object NotAuthorized : ShikimoriAuthState()

    data object Authorized: ShikimoriAuthState()

    data class Error(val authorizationException: AuthorizationException) : ShikimoriAuthState()

}
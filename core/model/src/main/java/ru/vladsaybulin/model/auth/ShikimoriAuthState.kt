package ru.vladsaybulin.model.auth

sealed class ShikimoriAuthState {
    data object NotAuthorized : ShikimoriAuthState()

    data object Authorized: ShikimoriAuthState()

    data class Error(val throwable: Throwable) : ShikimoriAuthState()

}
package ru.vladsaybulin.common.auth

fun interface LogoutAction {
    suspend fun logout()
}
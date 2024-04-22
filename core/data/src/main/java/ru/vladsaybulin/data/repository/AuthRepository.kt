package ru.vladsaybulin.data.repository

import ru.vladsaybulin.core.auth.ShikimoriAuthState
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.database.ShikiDatabase
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authorization: ShikimoriAuthorization,
    private val database: ShikiDatabase
) {
    fun isAuthorized() = authorization.authState.value == ShikimoriAuthState.Authorized

    suspend fun logOut() {
        authorization.signOut()
        database.userRateDao.deleteAll()
    }
}
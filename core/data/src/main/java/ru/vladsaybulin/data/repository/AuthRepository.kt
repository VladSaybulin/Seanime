package ru.vladsaybulin.data.repository

import kotlinx.coroutines.flow.StateFlow
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authorization: ShikimoriAuthorization,
    private val userRateDao: UserRateDao
) {
    val authState: StateFlow<ShikimoriAuthState> = authorization.authState

    fun isAuthorized() = authorization.authState.value == ShikimoriAuthState.Authorized

    suspend fun signOut() {
        authorization.signOut()
        userRateDao.deleteAll()
    }
}
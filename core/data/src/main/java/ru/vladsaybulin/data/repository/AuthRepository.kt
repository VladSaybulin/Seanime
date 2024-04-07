package ru.vladsaybulin.data.repository

import ru.vladsaybulin.core.auth.ShikimoriAuthState
import ru.vladsaybulin.database.ShikiDatabase
import ru.vladsaybulin.datastore.ShikiPreferencesDataSource
import ru.vladsaybulin.network.datasource.AuthDataSource
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authState: ShikimoriAuthState,
    private val database: ShikiDatabase,
    private val authDataSource: AuthDataSource,
    private val shikiPreferences: ShikiPreferencesDataSource
) {
    private fun isAuthorized() = authState.isAuthorized

    private suspend fun logOut() {
        authDataSource.signOut()
        authState.onLogout()
        database.userRateDao.deleteAll()
    }
}
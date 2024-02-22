package ru.vladsaybulin.data.repository

import ru.vladsaybulin.datastore.ShikiPreferencesDataSource
import ru.vladsaybulin.network.common.TokensHolder
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val shikiPreferencesDataSource: ShikiPreferencesDataSource
) : TokensHolder {

    override suspend fun getAccessToken(): String? {
        return null
    }

    override suspend fun refreshAccessToken(): String? {
        return null
    }

    fun logIn(authorizationCode: String) {
        TODO("Not implemented")
    }

    fun logOut() {
        TODO("Not implemented")
    }
}
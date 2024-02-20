package ru.vladsaybulin.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import ru.vladsaybulin.model.AuthTokens

class ShikiPreferencesDataSource @Inject constructor(
    private val authPreferencesDataStore: DataStore<AuthPreferences>,
) {
    val authTokens = authPreferencesDataStore.data.map { authPreferences ->
        authPreferences.takeIf { it === AuthPreferences.getDefaultInstance() }
            ?.let { AuthTokens(it.accessToken, it.refreshToken) }
    }
}
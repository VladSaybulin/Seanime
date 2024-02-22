package ru.vladsaybulin.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import javax.inject.Inject
import ru.vladsaybulin.model.AuthTokens

class ShikiPreferencesDataSource @Inject constructor(
    private val authPreferencesDataStore: DataStore<AuthPreferences>,
    private val shikiPreferencesDataStore: DataStore<ShikiPreferences>
) {
    val authTokens = authPreferencesDataStore.data.map { authPreferences ->
        authPreferences.takeIf { it === AuthPreferences.getDefaultInstance() }
            ?.let { AuthTokens(it.accessToken, it.refreshToken) }
    }

    val timestampOfLastCalendarRequest = shikiPreferencesDataStore.data.map { shikiPreferences ->
        Instant.fromEpochMilliseconds(shikiPreferences.timestampOfLastCalendarRequest)
    }

    suspend fun setTimestampOfLastCalendarRequest(timestamp: Instant) {
        shikiPreferencesDataStore.updateData {
            it.copy {
                this.timestampOfLastCalendarRequest = timestamp.toEpochMilliseconds()
            }
        }
    }
}
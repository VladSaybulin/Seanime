package ru.vladsaybulin.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import javax.inject.Inject

class ShikiPreferencesDataSource @Inject constructor(
    private val authPreferencesDataStore: DataStore<AuthPreferences>,
    private val shikiPreferencesDataStore: DataStore<ShikiPreferences>
) {
    val authStateJsonString = authPreferencesDataStore.data.map { it.authStateJsonText }

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

    suspend fun setAuthStateJsonString(newAuthStateJsonText: String) {
        authPreferencesDataStore.updateData {
            it.copy {
                this.authStateJsonText = newAuthStateJsonText
            }

        }
    }
}
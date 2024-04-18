package ru.vladsaybulin.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import ru.vladsaybulin.core.datastore.proto.ShikiPreferences
import ru.vladsaybulin.core.datastore.proto.copy
import javax.inject.Inject

class ShikiPreferencesDataSource @Inject constructor(
    private val shikiPreferencesDataStore: DataStore<ShikiPreferences>
) {
    val authStateJsonString = shikiPreferencesDataStore.data
        .map { it.auth.authStateJsonText }

    val calendarLastRequestDate = shikiPreferencesDataStore.data
        .map { Instant.fromEpochMilliseconds(it.lastCalendarRequestDate) }

    val animeGenresLastRequestDate = shikiPreferencesDataStore.data
        .map { Instant.fromEpochMilliseconds(it.lastAnimeGenresRequestDate) }

    val mangaGenresLastRequestDate = shikiPreferencesDataStore.data
        .map { Instant.fromEpochMilliseconds(it.lastMangaGenresRequestDate) }

    val studiosLastRequestDate = shikiPreferencesDataStore.data
        .map { Instant.fromEpochMilliseconds(it.lastStudiosRequestDate) }

    val publishersLastRequestDate = shikiPreferencesDataStore.data
        .map { Instant.fromEpochMilliseconds(it.lastPublishersRequestDate) }

    suspend fun setLastCalendarRequestDate(date: Instant) {
        shikiPreferencesDataStore.updateData {
            it.copy { lastCalendarRequestDate = date.toEpochMilliseconds() }
        }
    }

    suspend fun setLastAnimeGenresRequestDate(date: Instant) {
        shikiPreferencesDataStore.updateData {
            it.copy { lastAnimeGenresRequestDate = date.toEpochMilliseconds() }
        }
    }

    suspend fun setLastMangaGenresRequestDate(date: Instant) {
        shikiPreferencesDataStore.updateData {
            it.copy { lastMangaGenresRequestDate = date.toEpochMilliseconds() }
        }
    }

    suspend fun setLastStudiosRequestDate(date: Instant) {
        shikiPreferencesDataStore.updateData {
            it.copy { lastStudiosRequestDate = date.toEpochMilliseconds() }
        }
    }

    suspend fun setLastPublishersRequestDate(date: Instant) {
        shikiPreferencesDataStore.updateData {
            it.copy { lastPublishersRequestDate = date.toEpochMilliseconds() }
        }
    }

    suspend fun setAuthStateJsonString(newAuthStateJsonText: String) {
        shikiPreferencesDataStore.updateData {
            it.copy {
                auth = auth.copy { authStateJsonText = newAuthStateJsonText }
            }
        }
    }
}
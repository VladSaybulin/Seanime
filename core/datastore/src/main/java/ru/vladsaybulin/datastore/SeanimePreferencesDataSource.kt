package ru.vladsaybulin.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import ru.vladsaybulin.core.datastore.proto.SeanimePreferences
import ru.vladsaybulin.core.datastore.proto.copy
import javax.inject.Inject

class SeanimePreferencesDataSource @Inject constructor(
    private val seanimePreferencesDataStore: DataStore<SeanimePreferences>
) {
    val myId = seanimePreferencesDataStore.data
        .map { prefs -> prefs.myId.takeIf { it != NULL_MY_ID } }

    val calendarLastRequestDate = seanimePreferencesDataStore.data
        .map { Instant.fromEpochMilliseconds(it.lastCalendarRequestDate) }

    val animeGenresLastRequestDate = seanimePreferencesDataStore.data
        .map { Instant.fromEpochMilliseconds(it.lastAnimeGenresRequestDate) }

    val mangaGenresLastRequestDate = seanimePreferencesDataStore.data
        .map { Instant.fromEpochMilliseconds(it.lastMangaGenresRequestDate) }

    val studiosLastRequestDate = seanimePreferencesDataStore.data
        .map { Instant.fromEpochMilliseconds(it.lastStudiosRequestDate) }

    val publishersLastRequestDate = seanimePreferencesDataStore.data
        .map { Instant.fromEpochMilliseconds(it.lastPublishersRequestDate) }

    suspend fun setLastCalendarRequestDate(date: Instant) {
        seanimePreferencesDataStore.updateData {
            it.copy { lastCalendarRequestDate = date.toEpochMilliseconds() }
        }
    }

    suspend fun setLastAnimeGenresRequestDate(date: Instant) {
        seanimePreferencesDataStore.updateData {
            it.copy { lastAnimeGenresRequestDate = date.toEpochMilliseconds() }
        }
    }

    suspend fun setLastMangaGenresRequestDate(date: Instant) {
        seanimePreferencesDataStore.updateData {
            it.copy { lastMangaGenresRequestDate = date.toEpochMilliseconds() }
        }
    }

    suspend fun setLastStudiosRequestDate(date: Instant) {
        seanimePreferencesDataStore.updateData {
            it.copy { lastStudiosRequestDate = date.toEpochMilliseconds() }
        }
    }

    suspend fun setLastPublishersRequestDate(date: Instant) {
        seanimePreferencesDataStore.updateData {
            it.copy { lastPublishersRequestDate = date.toEpochMilliseconds() }
        }
    }

    suspend fun setMyId(newMyId: Long?) {
        seanimePreferencesDataStore.updateData {
            it.copy { myId = newMyId ?: NULL_MY_ID }
        }
    }
}

internal const val NULL_MY_ID = -1L
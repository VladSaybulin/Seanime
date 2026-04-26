/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
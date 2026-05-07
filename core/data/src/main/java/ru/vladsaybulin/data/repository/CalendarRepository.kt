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

package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.core.domain.repository.CalendarRepository as DomainCalendarRepository
import ru.vladsaybulin.data.model.animeShell
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.util.sync
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.CalendarDao
import ru.vladsaybulin.database.models.calendar.PopulatedCalendarItem
import ru.vladsaybulin.database.models.calendar.asExternalModel
import ru.vladsaybulin.datastore.SeanimePreferencesDataSource
import ru.vladsaybulin.model.calendar.CalendarItem
import ru.vladsaybulin.network.datasource.CalendarDataSource
import ru.vladsaybulin.network.models.calendar.NetworkCalendarItem
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class CalendarRepository @Inject constructor(
    private val calendarDataSource: CalendarDataSource,
    private val calendarDao: CalendarDao,
    private val animeDao: AnimeDao,
    private val seanimePreferencesDataSource: SeanimePreferencesDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : DomainCalendarRepository {
    override fun getCalendarItems(): Flow<List<CalendarItem>> =
        calendarDao.getAllCalendarItems()
            .onStart { syncCalendarItems() }
            .map { items -> items.map(PopulatedCalendarItem::asExternalModel) }
            .flowOn(ioDispatcher)

    override suspend fun refreshCalendarItems() {
        val response = calendarDataSource.getAllCalendarItems()
        calendarDao.deleteAllItems()
        animeDao.insertOrIgnoreAnimes(response.map(NetworkCalendarItem::animeShell))
        calendarDao.insertCalendarItems(response.map(NetworkCalendarItem::asEntity))
    }

    private suspend fun syncCalendarItems() {
        sync(
            ttl = CALENDAR_TTL,
            lastRequestDateFlow = seanimePreferencesDataSource.calendarLastRequestDate,
            updateLastRequest = seanimePreferencesDataSource::setLastCalendarRequestDate,
            refresh = ::refreshCalendarItems
        )
    }
}

private val CALENDAR_TTL = 1.hours
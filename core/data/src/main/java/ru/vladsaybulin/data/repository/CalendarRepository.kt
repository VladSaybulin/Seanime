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
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.TTLStrategies
import ru.vladsaybulin.core.domain.repository.CalendarRepository as DomainCalendarRepository
import ru.vladsaybulin.data.model.animeShell
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.request.RequestCoordinator
import ru.vladsaybulin.data.request.UpdateScope
import ru.vladsaybulin.data.request.cachedKey
import ru.vladsaybulin.data.withForceStrategy
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.CalendarDao
import ru.vladsaybulin.database.models.calendar.PopulatedCalendarItem
import ru.vladsaybulin.database.models.calendar.asExternalModel
import ru.vladsaybulin.database.models.lastrequest.RequestType
import ru.vladsaybulin.model.calendar.CalendarItem
import ru.vladsaybulin.network.datasource.CalendarDataSource
import ru.vladsaybulin.network.models.calendar.NetworkCalendarItem
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class CalendarRepository @Inject constructor(
    private val calendarDataSource: CalendarDataSource,
    private val calendarDao: CalendarDao,
    private val animeDao: AnimeDao,
    private val coordinator: RequestCoordinator,
    @param:Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : DomainCalendarRepository {
    override fun getCalendarItems(): Flow<List<CalendarItem>> =
        calendarDao.getAllCalendarItems()
            .map { items -> items.map(PopulatedCalendarItem::asExternalModel) }
            .flowOn(ioDispatcher)

    override suspend fun refreshCalendarItems(force: Boolean) {
        coordinator.sync(
            key = cachedKey(RequestType.Calendar),
            ttlStrategy = withForceStrategy(force) { TTLStrategies.Calendar },
            block = { updateCalendarItems() }
        )
    }

    private suspend fun UpdateScope.updateCalendarItems() {
        val response = calendarDataSource.getAllCalendarItems()

        val calendarItems = response.map(NetworkCalendarItem::asEntity)
        val animes = response.map(NetworkCalendarItem::animeShell)

        write {
            animeDao.insertOrIgnoreAnimes(animes)
            calendarDao.deleteAllItems()
            calendarDao.insertCalendarItems(calendarItems)
        }
    }
}

private val CALENDAR_TTL = 1.hours
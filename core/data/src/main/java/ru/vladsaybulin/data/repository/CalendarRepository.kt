package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.animeShell
import ru.vladsaybulin.data.model.asPOJO
import ru.vladsaybulin.data.util.sync
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.CalendarDao
import ru.vladsaybulin.database.models.calendar.PopulatedCalendarItem
import ru.vladsaybulin.database.models.calendar.asExternalModel
import ru.vladsaybulin.datastore.ShikiPreferencesDataSource
import ru.vladsaybulin.model.calendar.CalendarItem
import ru.vladsaybulin.network.datasource.CalendarDataSource
import ru.vladsaybulin.network.models.CalendarItemDto
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class CalendarRepository @Inject constructor(
    private val calendarDataSource: CalendarDataSource,
    private val calendarDao: CalendarDao,
    private val animeDao: AnimeDao,
    private val shikiPreferencesDataSource: ShikiPreferencesDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) {
    fun getCalendarItems(): Flow<List<CalendarItem>> =
        calendarDao.getAllCalendarItems()
            .onStart { syncCalendarItems() }
            .map { items -> items.map(PopulatedCalendarItem::asExternalModel) }
            .flowOn(ioDispatcher)

    suspend fun refreshCalendarItems() {
        val response = calendarDataSource.getAllCalendarItems()
        calendarDao.deleteAllItems()
        animeDao.upsertAnimes(response.map(CalendarItemDto::animeShell))
        calendarDao.insertCalendarItems(response.map(CalendarItemDto::asPOJO))
    }

    private suspend fun syncCalendarItems() {
        sync(
            ttl = CALENDAR_TTL,
            lastRequestDateFlow = shikiPreferencesDataSource.calendarLastRequestDate,
            updateLastRequest = shikiPreferencesDataSource::setLastCalendarRequestDate,
            refresh = ::refreshCalendarItems
        )
    }
}

private val CALENDAR_TTL = 1.hours
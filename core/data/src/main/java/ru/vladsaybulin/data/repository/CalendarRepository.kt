package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.*
import ru.vladsaybulin.data.repository.model.animeShell
import ru.vladsaybulin.data.repository.model.asDbo
import ru.vladsaybulin.database.ShikiDatabase
import ru.vladsaybulin.database.models.PopulatedCalendarItem
import ru.vladsaybulin.database.models.asExternalModel
import ru.vladsaybulin.datastore.ShikiPreferencesDataSource
import ru.vladsaybulin.model.CalendarItem
import ru.vladsaybulin.network.retrofit.ShikiUnauthorizedApi
import ru.vladsaybulin.network.retrofit.models.CalendarDto
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class CalendarRepository @Inject constructor(
    private val unauthorizedApi: ShikiUnauthorizedApi,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val database: ShikiDatabase,
    private val shikiPreferencesDataSource: ShikiPreferencesDataSource,
) {
    private val dao = database.calendarDao

    fun getCalendarItems(searchQuery: String): Flow<List<CalendarItem>> =
        dao.getAllCalendarItems()
            .onStart { sync() }
            .map { items -> items.map(PopulatedCalendarItem::asExternalModel) }
            .flowOn(ioDispatcher)

    private suspend fun sync() {
        val timestamp = shikiPreferencesDataSource.timestampOfLastCalendarRequest.first()

        if (timestamp + THRESHOLD > Clock.System.now()) return

        withContext(ioDispatcher) {
            val response = unauthorizedApi.getAllCalendarItems()
            database.withTransaction {
                dao.deleteAllItems()
                dao.insertOrReplaceAnimeEntities(response.map(CalendarDto::animeShell))
                dao.insertCalendarItems(response.map(CalendarDto::asDbo))
            }
            shikiPreferencesDataSource.setTimestampOfLastCalendarRequest(Clock.System.now())
        }
    }
}

private val THRESHOLD = 1.hours
package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.*
import ru.vladsaybulin.data.model.animeShell
import ru.vladsaybulin.data.model.asDbo
import ru.vladsaybulin.database.ShikiDatabase
import ru.vladsaybulin.database.models.PopulatedCalendarItem
import ru.vladsaybulin.database.models.asExternalModel
import ru.vladsaybulin.datastore.ShikiPreferencesDataSource
import ru.vladsaybulin.model.CalendarItem
import ru.vladsaybulin.network.datasource.CalendarDataSource
import ru.vladsaybulin.network.models.CalendarItemDto
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class CalendarRepository @Inject constructor(
    private val calendarDataSource: CalendarDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val database: ShikiDatabase,
    private val shikiPreferencesDataSource: ShikiPreferencesDataSource,
) {
    private val dao = database.calendarDao

    fun getCalendarItems(searchQuery: String? = null): Flow<List<CalendarItem>> =
        dao.getAllCalendarItems()
            .onStart { refresh(false) }
            .map { items -> items.map(PopulatedCalendarItem::asExternalModel) }
            .flowOn(ioDispatcher)

    suspend fun refresh(forceRefresh: Boolean = true) {
        if (!forceRefresh) {
            val timestamp = shikiPreferencesDataSource.timestampOfLastCalendarRequest
                .flowOn(ioDispatcher)
                .firstOrNull() ?: Instant.DISTANT_PAST
            if (timestamp + THRESHOLD > Clock.System.now()) return
        }

        withContext(ioDispatcher) {
            val response = calendarDataSource.getAllCalendarItems()

            database.withTransaction {
                dao.deleteAllItems()
                dao.insertOrReplaceAnimeEntities(response.map(CalendarItemDto::animeShell))
                dao.insertCalendarItems(response.map(CalendarItemDto::asDbo))
            }

            shikiPreferencesDataSource.setTimestampOfLastCalendarRequest(Clock.System.now())
        }
    }
}

private val THRESHOLD = 1.hours
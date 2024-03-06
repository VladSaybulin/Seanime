package ru.vladsaybulin.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.vladsaybulin.data.repository.CalendarRepository
import ru.vladsaybulin.model.CalendarDay
import javax.inject.Inject

class GetCalendarDaysUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository,
) {
    operator fun invoke(): Flow<List<CalendarDay>> =
        calendarRepository
            .getCalendarItems()
            .map { calendarItems ->
                val now = Clock.System.now()
                val timeZone = TimeZone.currentSystemDefault()
                calendarItems.groupBy { calendarItem ->
                    if (calendarItem.nextEpisodeAt > now) {
                        calendarItem.nextEpisodeAt.toLocalDateTime(timeZone).date
                    } else null
                }.map { (date, items) -> CalendarDay(date, items.sortedBy { it.nextEpisodeAt }) }
            }
}
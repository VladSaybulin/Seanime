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

package ru.vladsaybulin.core.domain.calendar

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.vladsaybulin.data.repository.CalendarRepository
import ru.vladsaybulin.model.calendar.CalendarItem
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

data class CalendarDay(
    val date: LocalDate?,
    val items: List<CalendarItem>
)
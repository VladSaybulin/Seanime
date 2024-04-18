package ru.vladsaybulin.model

import kotlinx.datetime.LocalDate
import ru.vladsaybulin.model.calendar.CalendarItem

data class CalendarDay(
    val date: LocalDate?,
    val items: List<CalendarItem>
)
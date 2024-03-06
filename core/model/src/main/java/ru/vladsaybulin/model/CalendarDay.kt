package ru.vladsaybulin.model

import kotlinx.datetime.LocalDate

data class CalendarDay(
    val date: LocalDate?,
    val items: List<CalendarItem>
)
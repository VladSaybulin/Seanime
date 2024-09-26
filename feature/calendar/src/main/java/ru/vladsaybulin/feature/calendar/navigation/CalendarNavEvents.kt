package ru.vladsaybulin.feature.calendar.navigation

import androidx.compose.runtime.Immutable

@Immutable
data class CalendarNavEvents(
    val navigateToAnimeDetails: (Long) -> Unit,
    val navigateToMe: () -> Unit
)
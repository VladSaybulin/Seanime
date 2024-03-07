package ru.vladsaybulin.feature.calendar.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.vladsaybulin.feature.calendar.CalendarRoute

private const val CALENDAR_ROUTE = "calendar"

fun NavController.navigateToCalendar() {
    navigate(CALENDAR_ROUTE)
}

fun NavGraphBuilder.calendarScreen(openAnimeDetails: (animeId: Long) -> Unit) {
    composable(route = CALENDAR_ROUTE) {
        CalendarRoute(
            openAnimeDetails = openAnimeDetails
        )
    }
}
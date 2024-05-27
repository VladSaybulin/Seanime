package ru.vladsaybulin.feature.calendar.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.vladsaybulin.feature.calendar.CalendarRoute

inline fun <reified Route : Any> NavGraphBuilder.calendarScreen(navEvents: CalendarNavEvents) {
    composable<Route> {
        CalendarRoute(navEvents = navEvents)
    }
}
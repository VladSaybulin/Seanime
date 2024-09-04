package ru.vladsaybulin.feature.calendar.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.calendar.CalendarRoute

@Serializable
object CalendarGraphRoute

@Serializable
private object CalendarScreenRoute

fun NavController.navigateToCalendarGraph(navOptions: NavOptions?) {
    navigate(CalendarGraphRoute, navOptions)
}

fun NavGraphBuilder.calendarGraph(
    navEvents: CalendarNavEvents,
    nested: NavGraphBuilder.() -> Unit
) {
    navigation<CalendarGraphRoute>(startDestination = CalendarScreenRoute) {
        composable<CalendarScreenRoute> {
            CalendarRoute(navEvents = navEvents)
        }
        nested()
    }
}
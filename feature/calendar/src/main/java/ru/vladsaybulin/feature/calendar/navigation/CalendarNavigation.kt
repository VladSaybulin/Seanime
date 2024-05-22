package ru.vladsaybulin.feature.calendar.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.vladsaybulin.core.navigation.SeanimeNavigator
import ru.vladsaybulin.feature.calendar.CalendarRoute

const val CALENDAR_GRAPH_ROUTE = "calendar_graph"
private const val CALENDAR_SCREEN_ROUTE = "$CALENDAR_GRAPH_ROUTE/calendar_route"

fun NavController.navigateToCalendarGraph(navOptions: NavOptions?) {
    navigate(CALENDAR_SCREEN_ROUTE, navOptions)
}

fun NavGraphBuilder.calendarGraph(
    navigator: SeanimeNavigator,
    nested: NavGraphBuilder.() -> Unit
) {
    navigation(
        startDestination = CALENDAR_SCREEN_ROUTE,
        route = CALENDAR_GRAPH_ROUTE
    ) {
        composable(route = CALENDAR_SCREEN_ROUTE) {
            CalendarRoute(navigator = navigator)
        }
        nested()
    }

}
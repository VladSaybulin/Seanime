package ru.vladsaybulin.feature.calendar.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.vladsaybulin.feature.calendar.CalendarRoute
import ru.vladsaybulin.model.common.EntryType

const val CALENDAR_ROUTE = "calendar"

fun NavController.navigateToCalendar(navOptions: NavOptions?) {
    navigate(CALENDAR_ROUTE, navOptions)
}

fun NavGraphBuilder.calendarScreen(
    onEntryClick: (EntryType, Long) -> Unit,
) {
    composable(route = CALENDAR_ROUTE) {
        CalendarRoute(
            onEntryClick = onEntryClick
        )
    }
}
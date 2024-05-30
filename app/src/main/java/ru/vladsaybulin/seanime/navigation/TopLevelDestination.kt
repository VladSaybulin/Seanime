package ru.vladsaybulin.seanime.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.feature.calendar.navigation.CALENDAR_GRAPH_ROUTE
import ru.vladsaybulin.feature.home.navigation.HOME_GRAPH_ROUTE
import ru.vladsaybulin.feature.list.navigation.MY_LIST_GRAPH_ROUTE
import ru.vladsaybulin.feature.search.navigation.SEARCH_GRAPH_ROUTE
import ru.vladsaybulin.feature.calendar.R as calendarR
import ru.vladsaybulin.feature.home.R as homeR
import ru.vladsaybulin.feature.list.R as listR
import ru.vladsaybulin.feature.search.R as searchR

enum class TopLevelDestination(
    val graphRoute: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME(
        graphRoute = HOME_GRAPH_ROUTE,
        selectedIcon = SeanimeIcons.Home,
        unselectedIcon = SeanimeIcons.OutlinedHome,
        iconTextId = homeR.string.feature_home_title,
        titleTextId = homeR.string.feature_home_title
    ),
    SEARCH(
        graphRoute = SEARCH_GRAPH_ROUTE,
        selectedIcon = SeanimeIcons.Search,
        unselectedIcon = SeanimeIcons.Search,
        iconTextId = searchR.string.feature_search_title,
        titleTextId = searchR.string.feature_search_title
    ),
    LIST(
        graphRoute = MY_LIST_GRAPH_ROUTE,
        selectedIcon = SeanimeIcons.Bookmark,
        unselectedIcon = SeanimeIcons.OutlinedBookmark,
        iconTextId = listR.string.feature_list_title,
        titleTextId = listR.string.feature_list_title,
    ),
    CALENDAR(
        graphRoute = CALENDAR_GRAPH_ROUTE,
        selectedIcon = SeanimeIcons.CalendarToday,
        unselectedIcon = SeanimeIcons.CalendarToday,
        iconTextId = calendarR.string.feature_calendar_title,
        titleTextId = calendarR.string.feature_calendar_title,
    )
}
package ru.vladsaybulin.seanime.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.seanime.navigation.routes.CalendarGraph
import ru.vladsaybulin.seanime.navigation.routes.HomeGraph
import ru.vladsaybulin.seanime.navigation.routes.MyListGraph
import ru.vladsaybulin.seanime.navigation.routes.SearchGraph
import ru.vladsaybulin.feature.calendar.R as calendarR
import ru.vladsaybulin.feature.home.R as homeR
import ru.vladsaybulin.feature.list.R as listR
import ru.vladsaybulin.feature.search.R as searchR

enum class TopLevelDestination(
    val graphRoute: Any,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME(
        graphRoute = HomeGraph,
        selectedIcon = ShikimoriIcons.Home,
        unselectedIcon = ShikimoriIcons.OutlinedHome,
        iconTextId = homeR.string.feature_home_title,
        titleTextId = homeR.string.feature_home_title
    ),
    SEARCH(
        graphRoute = SearchGraph,
        selectedIcon = ShikimoriIcons.Search,
        unselectedIcon = ShikimoriIcons.Search,
        iconTextId = searchR.string.feature_search_title,
        titleTextId = searchR.string.feature_search_title
    ),
    LIST(
        graphRoute = MyListGraph,
        selectedIcon = ShikimoriIcons.Bookmark,
        unselectedIcon = ShikimoriIcons.OutlinedBookmark,
        iconTextId = listR.string.feature_list_title,
        titleTextId = listR.string.feature_list_title,
    ),
    CALENDAR(
        graphRoute = CalendarGraph,
        selectedIcon = ShikimoriIcons.CalendarToday,
        unselectedIcon = ShikimoriIcons.CalendarToday,
        iconTextId = calendarR.string.feature_calendar_title,
        titleTextId = calendarR.string.feature_calendar_title,
    )
}
/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.seanime.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.feature.calendar.navigation.CalendarGraphRoute
import ru.vladsaybulin.feature.home.navigation.HomeGraphRoute
import ru.vladsaybulin.feature.list.navigation.ListGraphRoute
import ru.vladsaybulin.feature.search.navigation.SearchGraphRoute
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
        graphRoute = HomeGraphRoute,
        selectedIcon = SeanimeIcons.Home,
        unselectedIcon = SeanimeIcons.OutlinedHome,
        iconTextId = homeR.string.feature_home_title,
        titleTextId = homeR.string.feature_home_title
    ),
    SEARCH(
        graphRoute = SearchGraphRoute,
        selectedIcon = SeanimeIcons.Search,
        unselectedIcon = SeanimeIcons.Search,
        iconTextId = searchR.string.feature_search_title,
        titleTextId = searchR.string.feature_search_title
    ),
    LIST(
        graphRoute = ListGraphRoute,
        selectedIcon = SeanimeIcons.Bookmark,
        unselectedIcon = SeanimeIcons.OutlinedBookmark,
        iconTextId = listR.string.feature_list_title,
        titleTextId = listR.string.feature_list_title,
    ),
    CALENDAR(
        graphRoute = CalendarGraphRoute,
        selectedIcon = SeanimeIcons.CalendarToday,
        unselectedIcon = SeanimeIcons.CalendarToday,
        iconTextId = calendarR.string.feature_calendar_title,
        titleTextId = calendarR.string.feature_calendar_title,
    )
}
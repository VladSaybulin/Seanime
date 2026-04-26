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
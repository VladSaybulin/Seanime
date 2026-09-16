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

package ru.vladsaybulin.feature.calendar.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.core.navigation.SeanimeNavKey
import ru.vladsaybulin.feature.calendar.api.navigation.CalendarNavKey
import ru.vladsaybulin.feature.calendar.impl.CalendarRoute
import ru.vladsaybulin.feature.calendar.impl.CalendarViewModel
import ru.vladsaybulin.feature.list.title.details.navigation.navigateToAnime
import ru.vladsaybulin.feature.profile.api.navigation.navigateToProfile

/**
 * Entry provider for the calendar feature.
 */
context(navigator: Navigator)
fun EntryProviderScope<SeanimeNavKey>.calendarEntry() {
    entry<CalendarNavKey> {
        val viewModel = hiltViewModel<CalendarViewModel>()

        CalendarRoute(
            viewModel = viewModel,
            onAnimeClick = navigator::navigateToAnime,
            onMyProfileClick = navigator::navigateToProfile
        )
    }
}
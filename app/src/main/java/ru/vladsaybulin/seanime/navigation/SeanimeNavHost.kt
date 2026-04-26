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

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.vladsaybulin.feature.title.authors.navigation.titleAuthorsScreen
import ru.vladsaybulin.feature.calendar.navigation.calendarGraph
import ru.vladsaybulin.feature.character.navigation.characterDetailsScreen
import ru.vladsaybulin.feature.title.details.navigation.titleDetailsScreen
import ru.vladsaybulin.feature.home.navigation.homeGraph
import ru.vladsaybulin.feature.list.navigation.listGraph
import ru.vladsaybulin.feature.list.navigation.listScreen
import ru.vladsaybulin.feature.search.navigation.searchGraph
import ru.vladsaybulin.feature.search.navigation.searchScreen
import ru.vladsaybulin.feature.title.characters.navigation.titleCharactersScreen
import ru.vladsaybulin.feature.title.related.navigation.titleRelatedScreen
import ru.vladsaybulin.feature.title.screenshots.navigation.animeScreenshotsScreen
import ru.vladsaybulin.feature.title.videos.navigation.animeVideosScreen
import ru.vladsaybulin.feature.profile.navigation.profileScreen

@Composable
fun SeanimeNavHost(
    navController: NavHostController,
    navEventsFactory: SeanimeNavEventsFactory,
    startDestination: TopLevelDestination = TopLevelDestination.HOME
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.graphRoute
    ) {
        homeGraph(navEventsFactory.createHomeNavEvents()) {
            sharedScreens(navEventsFactory, TopLevelDestination.HOME)
        }
        searchGraph(navEventsFactory.createSearchNavEvents()) {
            sharedScreens(navEventsFactory, TopLevelDestination.SEARCH)
        }
        listGraph(navEventsFactory.createListNavEvents()) {
            sharedScreens(navEventsFactory, TopLevelDestination.LIST)
        }
        calendarGraph(navEventsFactory.createCalendarNavEvents()) {
            sharedScreens(navEventsFactory, TopLevelDestination.CALENDAR)
        }
    }
}

fun NavGraphBuilder.sharedScreens(
    navEventsFactory: SeanimeNavEventsFactory,
    topLevelDestination: TopLevelDestination
) {
    if (topLevelDestination != TopLevelDestination.SEARCH) {
        searchScreen(navEventsFactory.createSearchNavEvents())
    }

    if (topLevelDestination != TopLevelDestination.LIST) {
        listScreen(navEventsFactory.createListNavEvents())
    }

    titleDetailsScreen(navEventsFactory.createTitleDetailNavEvents())
    titleAuthorsScreen(navEventsFactory.createTitleAuthorsNavEvents())
    titleRelatedScreen(navEventsFactory.createTitleRelatedNavEVents())
    titleCharactersScreen(navEventsFactory.createTitleCharactersNavEvents())
    animeScreenshotsScreen(navEventsFactory.createAnimeScreenshotsNavEvents())
    animeVideosScreen(navEventsFactory.createAnimeVideosNavEvents())

    characterDetailsScreen(navEventsFactory.createCharacterDetailsNavEvents())

    profileScreen()
}
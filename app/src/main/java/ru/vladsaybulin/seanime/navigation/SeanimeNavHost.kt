package ru.vladsaybulin.seanime.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.vladsaybulin.feature.authors.navigation.titleAuthorsScreen
import ru.vladsaybulin.feature.calendar.navigation.calendarGraph
import ru.vladsaybulin.feature.character.navigation.characterDetailsScreen
import ru.vladsaybulin.feature.details.navigation.titleDetailsScreen
import ru.vladsaybulin.feature.home.navigation.homeGraph
import ru.vladsaybulin.feature.list.navigation.listGraph
import ru.vladsaybulin.feature.list.navigation.listScreen
import ru.vladsaybulin.feature.search.navigation.searchGraph
import ru.vladsaybulin.feature.search.navigation.searchScreen

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

    characterDetailsScreen(navEventsFactory.createCharacterDetailsNavEvents())
}
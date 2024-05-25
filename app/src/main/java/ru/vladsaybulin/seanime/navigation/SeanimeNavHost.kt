package ru.vladsaybulin.seanime.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import ru.vladsaybulin.core.navigation.SeanimeNavigator
import ru.vladsaybulin.feature.calendar.navigation.calendarGraph
import ru.vladsaybulin.feature.character.navigation.characterDetailsScreen
import ru.vladsaybulin.feature.details.navigation.detailsScreen
import ru.vladsaybulin.feature.home.navigation.homeGraph
import ru.vladsaybulin.feature.list.navigation.listGraph
import ru.vladsaybulin.feature.list.navigation.listScreen
import ru.vladsaybulin.feature.search.navigation.searchGraph
import ru.vladsaybulin.feature.search.navigation.searchScreen
import ru.vladsaybulin.seanime.ui.SeanimeAppState

@Composable
fun SeanimeNavHost(
    seanimeAppState: SeanimeAppState,
    startDestination: String = TopLevelDestination.HOME.graphRoute
) {
    val navController = seanimeAppState.navController

    val nested: NavGraphBuilder.(TopLevelDestination) -> Unit = {
        nestedScreens(
            topLevelDestination = it,
            navigator = seanimeAppState.navigator
        )
    }

    NavHost(
        navController = seanimeAppState.navController,
        startDestination = startDestination,
    ) {
        homeGraph(
            navigator = seanimeAppState.navigator,
            nested = { nested(TopLevelDestination.HOME) }
        )

        searchGraph(
            navigator = seanimeAppState.navigator,
            nested = { nested(TopLevelDestination.SEARCH) }
        )

        calendarGraph(
            navigator = seanimeAppState.navigator,
            nested = { nested(TopLevelDestination.CALENDAR) }
        )

        listGraph(
            navigator = seanimeAppState.navigator,
            nested = { nested(TopLevelDestination.LIST) },
        )
    }
}

private fun NavGraphBuilder.nestedScreens(
    topLevelDestination: TopLevelDestination,
    navigator: SeanimeNavigator
) {
    detailsScreen(navigator = navigator)

    characterDetailsScreen(navigator = navigator)

    if (topLevelDestination != TopLevelDestination.SEARCH) {
        searchScreen(navigator = navigator)
    }

    if (topLevelDestination != TopLevelDestination.LIST) {
        listScreen(navigator = navigator)
    }
}
package ru.vladsaybulin.shikimori.navigation

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
import ru.vladsaybulin.shikimori.ui.ShikimoriAppState

@Composable
fun ShikimoriNavHost(
    shikimoriAppState: ShikimoriAppState,
    startDestination: String = TopLevelDestination.HOME.graphRoute
) {
    val navController = shikimoriAppState.navController

    val nested: NavGraphBuilder.(TopLevelDestination) -> Unit = {
        nestedScreens(
            topLevelDestination = it,
            navigator = shikimoriAppState.navigator
        )
    }

    NavHost(
        navController = shikimoriAppState.navController,
        startDestination = startDestination,
    ) {
        homeGraph(
            navigator = shikimoriAppState.navigator,
            nested = { nested(TopLevelDestination.HOME) }
        )

        searchGraph(
            navigator = shikimoriAppState.navigator,
            nested = { nested(TopLevelDestination.SEARCH) }
        )

        calendarGraph(
            navigator = shikimoriAppState.navigator,
            nested = { nested(TopLevelDestination.CALENDAR) }
        )

        listGraph(
            navigator = shikimoriAppState.navigator,
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
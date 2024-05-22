package ru.vladsaybulin.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.vladsaybulin.core.navigation.SeanimeNavigator
import ru.vladsaybulin.core.navigation.args.EntryDetailsArgs
import ru.vladsaybulin.core.navigation.args.SearchArgs
import ru.vladsaybulin.feature.home.HomeRoute

const val HOME_GRAPH_ROUTE = "home_graph"
private const val HOME_SCREEN_ROUTE = "home_route"

fun NavController.navigateToHomeGraph(navOptions: NavOptions) {
    navigate(HOME_GRAPH_ROUTE, navOptions)
}

fun NavGraphBuilder.homeGraph(
    navigator: SeanimeNavigator,
    nested: NavGraphBuilder.() -> Unit
) {
    navigation(
        startDestination = "$HOME_GRAPH_ROUTE/$HOME_SCREEN_ROUTE",
        route = HOME_GRAPH_ROUTE
    ) {
        homeScreen(navigator = navigator)
        nested()
    }
}

private fun NavGraphBuilder.homeScreen(navigator: SeanimeNavigator) {
    composable(route = "$route/$HOME_SCREEN_ROUTE") {
        HomeRoute(navigator = navigator)
    }
}
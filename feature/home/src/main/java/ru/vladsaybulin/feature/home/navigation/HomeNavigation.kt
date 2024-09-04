package ru.vladsaybulin.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.home.HomeScreen

@Serializable
object HomeGraphRoute

@Serializable
private object HomeScreenRoute

fun NavController.navigateToHomeGraph(navOptions: NavOptions?) {
    navigate(HomeGraphRoute, navOptions)
}

fun NavGraphBuilder.homeGraph(
    navEvents: HomeNavEvents,
    nested: NavGraphBuilder.() -> Unit
) {
    navigation<HomeGraphRoute>(startDestination = HomeScreenRoute) {
        composable<HomeScreenRoute> {
            HomeScreen(navEvents)
        }
        nested()
    }
}
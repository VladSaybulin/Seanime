package ru.vladsaybulin.seanime.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import ru.vladsaybulin.seanime.navigation.TopLevelDestination
import ru.vladsaybulin.seanime.navigation.TopLevelDestination.CALENDAR
import ru.vladsaybulin.seanime.navigation.TopLevelDestination.HOME
import ru.vladsaybulin.seanime.navigation.TopLevelDestination.LIST
import ru.vladsaybulin.seanime.navigation.TopLevelDestination.SEARCH
import ru.vladsaybulin.seanime.navigation.routes.CalendarGraph
import ru.vladsaybulin.seanime.navigation.routes.HomeGraph
import ru.vladsaybulin.seanime.navigation.routes.MyListGraph
import ru.vladsaybulin.seanime.navigation.routes.SearchGraph

@Composable
fun rememberSeanimeAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController()
) = remember(
    windowSizeClass,
    navController
) {
    SeanimeAppState(
        navController = navController,
        windowSizeClass = windowSizeClass
    )
}

@Stable
class SeanimeAppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass
) {
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        @Composable get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        @Composable get() = !shouldShowBottomBar

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            HOME -> navController.navigate(HomeGraph, navOptions = topLevelNavOptions)
            SEARCH -> navController.navigate(SearchGraph, navOptions = topLevelNavOptions)
            CALENDAR -> navController.navigate(CalendarGraph, navOptions = topLevelNavOptions)
            LIST -> navController.navigate(MyListGraph, navOptions = topLevelNavOptions)
        }
    }
}
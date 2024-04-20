package ru.vladsaybulin.shikimori.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.util.fastAny
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import ru.vladsaybulin.feature.calendar.navigation.navigateToCalendarGraph
import ru.vladsaybulin.feature.home.navigation.navigateToHomeGraph
import ru.vladsaybulin.feature.imageview.navigation.IMAGE_VIEW_ROUTE
import ru.vladsaybulin.feature.list.navigation.navigateToMyListGraph
import ru.vladsaybulin.feature.search.navigation.navigateToSearchGraph
import ru.vladsaybulin.shikimori.navigation.TopLevelDestination
import ru.vladsaybulin.shikimori.navigation.TopLevelDestination.CALENDAR
import ru.vladsaybulin.shikimori.navigation.TopLevelDestination.HOME
import ru.vladsaybulin.shikimori.navigation.TopLevelDestination.LIST
import ru.vladsaybulin.shikimori.navigation.TopLevelDestination.SEARCH

@Composable
fun rememberShikimoriAppState(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController()
) = remember(
    windowSizeClass,
    navController
) {
    ShikimoriAppState(
        navController = navController,
        windowSizeClass = windowSizeClass
    )
}

@Stable
class ShikimoriAppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
) {
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        @Composable get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact &&
                currentDestination.navigationVisible()

    val shouldShowNavRail: Boolean
        @Composable get() = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact &&
                currentDestination.navigationVisible()

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
            HOME -> navController.navigateToHomeGraph(navOptions = topLevelNavOptions)
            SEARCH -> navController.navigateToSearchGraph(navOptions = topLevelNavOptions)
            CALENDAR -> navController.navigateToCalendarGraph(navOptions = topLevelNavOptions)
            LIST -> navController.navigateToMyListGraph(navOptions = topLevelNavOptions)
        }
    }
}

private fun NavDestination?.navigationVisible() =
    this?.route?.let { r ->
        !HideNavigationOnDestinations.fastAny { r == it }
    } ?: true

private val HideNavigationOnDestinations = listOf(IMAGE_VIEW_ROUTE)
package ru.vladsaybulin.seanime.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import ru.vladsaybulin.feature.calendar.navigation.navigateToCalendarGraph
import ru.vladsaybulin.feature.home.navigation.navigateToHomeGraph
import ru.vladsaybulin.feature.imageview.ImageSet
import ru.vladsaybulin.feature.list.navigation.navigateToMyListGraph
import ru.vladsaybulin.feature.search.navigation.navigateToSearchGraph
import ru.vladsaybulin.seanime.navigation.SeanimeNavigatorImpl
import ru.vladsaybulin.seanime.navigation.TopLevelDestination
import ru.vladsaybulin.seanime.navigation.TopLevelDestination.CALENDAR
import ru.vladsaybulin.seanime.navigation.TopLevelDestination.HOME
import ru.vladsaybulin.seanime.navigation.TopLevelDestination.LIST
import ru.vladsaybulin.seanime.navigation.TopLevelDestination.SEARCH

@Composable
fun rememberSeanimeAppState(
    windowSizeClass: WindowSizeClass,
    onAuth: () -> Unit,
    onExternalLink: (String) -> Unit,
    navController: NavHostController = rememberNavController()
) = remember(
    windowSizeClass,
    onAuth,
    onExternalLink,
    navController
) {
    SeanimeAppState(
        navController = navController,
        windowSizeClass = windowSizeClass,
        onAuth = onAuth,
        onExternalLink = onExternalLink
    )
}

@Stable
class SeanimeAppState(
    val navController: NavHostController,
    val windowSizeClass: WindowSizeClass,
    onAuth: () -> Unit,
    onExternalLink: (String) -> Unit,
) {
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        @Composable get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        @Composable get() = !shouldShowBottomBar

    private val _imageViewSet = mutableStateOf<ImageSet>(ImageSet.NoSet)
    val imageViewSet get() = _imageViewSet.value

    val navigator = SeanimeNavigatorImpl(
        navController = navController,
        onImageView = { images, initialImage ->
            _imageViewSet.value = ImageSet.Set(images, initialImage)
        },
        onExternalClick = onExternalLink,
        onAuth = onAuth
    )

    fun hideImageView() {
        _imageViewSet.value = ImageSet.NoSet
    }

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
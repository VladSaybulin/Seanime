package ru.vladsaybulin.seanime.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.feature.userrate.UserRateBottomSheet
import ru.vladsaybulin.feature.userrate.UserRateViewModel
import ru.vladsaybulin.seanime.navigation.SeanimeNavHost
import ru.vladsaybulin.seanime.navigation.TopLevelDestination
import ru.vladsaybulin.seanime.navigation.navigator.SeanimeNavEventsFactory

@Composable
fun SeanimeApp(
    appState: SeanimeAppState,
    openUrl: (String) -> Unit,
    onAuth: () -> Unit
) {
    SeanimeTheme {
        Scaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    SeanimeBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination
                    )
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { padding ->
            Row(
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding)
            ) {
                if (appState.shouldShowNavRail) {
                    SeanimeNavRail(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination
                    )
                }

                SeanimeNavHost(
                    navController = appState.navController,
                    navEventsFactory = SeanimeNavEventsFactory(
                        navController = appState.navController,
                        openUrl = openUrl,
                        onAuth = onAuth,
                        openUserRateEditor = { },
                        openFullscreenImage = { _, _ -> }
                    )
                )
            }
        }
        val userRateViewModel = hiltViewModel<UserRateViewModel>()

        UserRateBottomSheet(
            viewModel = userRateViewModel,
            onDismissRequest = { userRateViewModel.hide() }
        )
    }
}

@Composable
private fun SeanimeNavRail(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier) {
        destinations.fastForEach {
            val selected = currentDestination.isTopLevelDestinationInHierarchy(it)
            NavigationRailItem(
                selected = selected,
                onClick = { onNavigateToDestination(it) },
                icon = {
                    if (selected) {
                        Icon(
                            imageVector = it.selectedIcon,
                            contentDescription = stringResource(id = it.iconTextId)
                        )
                    } else {
                        Icon(
                            imageVector = it.unselectedIcon,
                            contentDescription = stringResource(id = it.iconTextId)
                        )
                    }
                },
                label = { Text(stringResource(id = it.titleTextId)) }
            )
        }
    }
}

@Composable
private fun SeanimeBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    BottomAppBar(modifier = modifier) {
        destinations.fastForEach {
            val selected = currentDestination.isTopLevelDestinationInHierarchy(it)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(it) },
                icon = {
                    if (selected) {
                        Icon(
                            imageVector = it.selectedIcon,
                            contentDescription = stringResource(id = it.iconTextId)
                        )
                    } else {
                        Icon(
                            imageVector = it.unselectedIcon,
                            contentDescription = stringResource(id = it.iconTextId)
                        )
                    }
                },
                label = { Text(stringResource(id = it.titleTextId)) }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any { it.route == destination.graphRoute } ?: false
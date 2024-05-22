package ru.vladsaybulin.shikimori.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.feature.imageview.ImageViewViewModel
import ru.vladsaybulin.feature.imageview.navigation.navigateToImageView
import ru.vladsaybulin.feature.userrate.UserRateBottomSheet
import ru.vladsaybulin.feature.userrate.UserRateViewModel
import ru.vladsaybulin.shikimori.navigation.ShikimoriNavHost
import ru.vladsaybulin.shikimori.navigation.TopLevelDestination

@Composable
fun ShikimoriApp(
    appState: ShikimoriAppState,
    signIn: () -> Unit,
) {
    val imageViewViewModel = hiltViewModel<ImageViewViewModel>()
    val userRateViewModel = hiltViewModel<UserRateViewModel>()

    var showUserRateBottomSheet by remember { mutableStateOf(false) }
    var showRequireAuthDialog by remember { mutableStateOf(false) }

    ShikimoriTheme {
        Surface {
            Box(
                modifier = Modifier.fillMaxSize().navigationBarsPadding()
            ) {
                val screenPadding = PaddingValues(
                    bottom = if (appState.shouldShowBottomBar) 80.dp else 0.dp,
                    start = if (appState.shouldShowNavRail) 80.dp else 0.dp
                )
                CompositionLocalProvider(value = LocalScreenContentPadding provides screenPadding) {
                    ShikimoriNavHost(
                        shikimoriAppState = appState,
                        imageViewViewModel = imageViewViewModel,
                        onShowRequireAuthDialog = { showRequireAuthDialog = true },
                        onShowUserRate = {
                            userRateViewModel.setUserRate(it)
                            showUserRateBottomSheet = true
                        },
                        onShowImage = {
                            imageViewViewModel.setImages(it)
                            appState.navController.navigateToImageView()
                        },
                        onSignIn = signIn
                    )
                }

                if (appState.shouldShowNavRail) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = appState.isNavigationVisible,
                        enter = slideInHorizontally { -it },
                        exit = slideOutHorizontally { -it },
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        ShikimoriNavRail(
                            destinations = appState.topLevelDestinations,
                            onNavigateToDestination = appState::navigateToTopLevelDestination,
                            currentDestination = appState.currentDestination
                        )
                    }
                }
                if (appState.shouldShowBottomBar) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = appState.isNavigationVisible,
                        enter = slideInVertically { it } + fadeIn(),
                        exit = slideOutVertically { it } + fadeOut(),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        ShikimoriBottomBar(
                            destinations = appState.topLevelDestinations,
                            onNavigateToDestination = appState::navigateToTopLevelDestination,
                            currentDestination = appState.currentDestination
                        )
                    }
                }
            }
        }

        if (showUserRateBottomSheet) {
            UserRateBottomSheet(
                viewModel = userRateViewModel,
                onDismissRequest = { showUserRateBottomSheet = false }
            )
        }

        if (showRequireAuthDialog) {
            RequireAuthDialog(
                onSignIn = signIn,
                onDismissRequest = { showRequireAuthDialog = false }
            )
        }
    }
}

@Composable
private fun ShikimoriNavRail(
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
private fun ShikimoriBottomBar(
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
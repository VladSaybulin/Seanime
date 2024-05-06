package ru.vladsaybulin.shikimori.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import ru.vladsaybulin.core.navigation.NavigationEvent
import ru.vladsaybulin.core.navigation.args.EntryDetailsArgs
import ru.vladsaybulin.core.navigation.args.ImageViewArgs
import ru.vladsaybulin.core.navigation.args.SearchArgs
import ru.vladsaybulin.feature.calendar.navigation.calendarGraph
import ru.vladsaybulin.feature.character.navigation.characterDetailsScreen
import ru.vladsaybulin.feature.character.navigation.navigateToCharacter
import ru.vladsaybulin.feature.details.navigation.detailsScreen
import ru.vladsaybulin.feature.details.navigation.navigateToEntryDetails
import ru.vladsaybulin.feature.home.navigation.homeGraph
import ru.vladsaybulin.feature.imageview.ImageViewViewModel
import ru.vladsaybulin.feature.imageview.navigation.imageViewScreen
import ru.vladsaybulin.feature.list.navigation.listGraph
import ru.vladsaybulin.feature.list.navigation.listScreen
import ru.vladsaybulin.feature.search.navigation.navigateToSearch
import ru.vladsaybulin.feature.search.navigation.searchGraph
import ru.vladsaybulin.feature.search.navigation.searchScreen
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import ru.vladsaybulin.shikimori.ui.ShikimoriAppState

@Composable
fun ShikimoriNavHost(
    shikimoriAppState: ShikimoriAppState,
    imageViewViewModel: ImageViewViewModel,
    startDestination: String = TopLevelDestination.HOME.graphRoute,
    onShowUserRate: (UserRateWithEntry) -> Unit,
    onShowImage: (ImageViewArgs) -> Unit,
    onSignIn: () -> Unit,
    onShowRequireAuthDialog: () -> Unit,
) {
    val navController = shikimoriAppState.navController

    val nested: NavGraphBuilder.(TopLevelDestination) -> Unit = {
        nestedScreens(
            topLevelDestination = it,
            navController = navController,
            onEntryClick = navController::navigateToEntryDetails,
            onSearchClick = navController::navigateToSearch,
            onAuthorClick = {},
            onCharacterClick = navController::navigateToCharacter,
            onShowRequireAuthDialog = onShowRequireAuthDialog,
            onShowUserRate = onShowUserRate,
            onShowImage = onShowImage,
            onSignIn = onSignIn,
            onBackClick = navController::navigateUp
        )
    }

    NavHost(
        navController = shikimoriAppState.navController,
        startDestination = startDestination,
    ) {
        homeGraph(
            onEntryClick = navController::navigateToEntryDetails,
            onSearchClick = navController::navigateToSearch,
            onAllNewsTopicsClick = {},
            nested = { nested(TopLevelDestination.HOME) }
        )

        searchGraph(
            onEntryClick = navController::navigateToEntryDetails,
            nested = { nested(TopLevelDestination.SEARCH) }
        )

        calendarGraph(
            onEntryClick = navController::navigateToEntryDetails,
            nested = { nested(TopLevelDestination.CALENDAR) }
        )

        listGraph(
            onEntryClick = navController::navigateToEntryDetails,
            nested = { nested(TopLevelDestination.LIST) },
            onSignIn = onSignIn
        )

        imageViewScreen(
            viewModel = imageViewViewModel,
            onBackClick = navController::navigateUp
        )
    }
}

private fun NavGraphBuilder.nestedScreens(
    topLevelDestination: TopLevelDestination,
    navController: NavController,
    onEntryClick: (EntryDetailsArgs) -> Unit,
    onSearchClick: (SearchArgs) -> Unit,
    onAuthorClick: (Long) -> Unit,
    onCharacterClick: (Long) -> Unit,
    onShowRequireAuthDialog: () -> Unit,
    onShowUserRate: (UserRateWithEntry) -> Unit,
    onShowImage: (ImageViewArgs) -> Unit,
    onSignIn: () -> Unit,
    onBackClick: () -> Unit,
) {
    detailsScreen(
        onEntryClick = onEntryClick,
        onSearchClick = onSearchClick,
        onAuthorClick = onAuthorClick,
        onCharacterClick = onCharacterClick,
        onShowRequireAuthDialog = onShowRequireAuthDialog,
        onShowUserRate = onShowUserRate,
        onShowImage = onShowImage,
        onBackClick = onBackClick,
    )

    characterDetailsScreen(
        onNavigationEvent = { onNavigationEvent(navController, it) }
    )

    if (topLevelDestination != TopLevelDestination.SEARCH) {
        searchScreen(onEntryClick = onEntryClick)
    }

    if (topLevelDestination != TopLevelDestination.LIST) {
        listScreen(
            onEntryClick = onEntryClick,
            onSignIn = onSignIn
        )
    }
}

fun onNavigationEvent(
    navController: NavController,
    event: NavigationEvent
) {
    when (event) {
        NavigationEvent.Back -> navController.navigateUp()
        is NavigationEvent.CharacterDetails -> navController.navigateToCharacter(event.characterId)
        is NavigationEvent.EntryDetails -> navController.navigateToEntryDetails(EntryDetailsArgs(event.entryType, event.entryId))
    }
}
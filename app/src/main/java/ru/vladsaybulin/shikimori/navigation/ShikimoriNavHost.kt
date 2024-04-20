package ru.vladsaybulin.shikimori.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import ru.vladsaybulin.core.navigation.ImageViewArgs
import ru.vladsaybulin.feature.calendar.navigation.calendarScreen
import ru.vladsaybulin.feature.details.navigation.detailsScreen
import ru.vladsaybulin.feature.details.navigation.navigateToEntryDetails
import ru.vladsaybulin.feature.home.navigation.HOME_ROUTE
import ru.vladsaybulin.feature.home.navigation.homeScreen
import ru.vladsaybulin.feature.imageview.ImageViewViewModel
import ru.vladsaybulin.feature.imageview.navigation.imageViewScreen
import ru.vladsaybulin.feature.list.navigation.myListScreen
import ru.vladsaybulin.feature.search.navigation.navigateToSearch
import ru.vladsaybulin.feature.search.navigation.searchScreen
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import ru.vladsaybulin.shikimori.ui.ShikimoriAppState

@Composable
fun ShikimoriNavHost(
    shikimoriAppState: ShikimoriAppState,
    startDestination: String = HOME_ROUTE,
    imageViewViewModel: ImageViewViewModel,
    onShowUserRate: (UserRateWithEntry) -> Unit,
    onShowImage: (ImageViewArgs) -> Unit,
    onShowRequireAuthDialog: () -> Unit,
) {
    val navController = shikimoriAppState.navController

    NavHost(
        navController = shikimoriAppState.navController,
        startDestination = startDestination,
    ) {
        homeScreen(
            onEntryClick = navController::navigateToEntryDetails,
            onSearchClick = navController::navigateToSearch,
            onAllNewsTopicsClick = {}
        )

        searchScreen(onEntryClick = navController::navigateToEntryDetails)

        calendarScreen(
            onEntryClick = navController::navigateToEntryDetails
        )

        myListScreen(
            onEntryClick = navController::navigateToEntryDetails,
        )

        detailsScreen(
            onEntryClick = navController::navigateToEntryDetails,
            onSearchClick = navController::navigateToSearch,
            onAuthorClick = {},
            onCharacterClick = {},
            onShowRequireAuthDialog = onShowRequireAuthDialog,
            onShowUserRate = onShowUserRate,
            onShowImage = onShowImage,
            onBackClick = navController::navigateUp,
        )

        imageViewScreen(
            viewModel = imageViewViewModel,
            onBackClick = navController::navigateUp
        )
    }
}
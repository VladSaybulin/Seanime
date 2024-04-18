package ru.vladsaybulin.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import ru.vladsaybulin.core.navigation.SearchArgs
import ru.vladsaybulin.feature.home.HomeRoute
import ru.vladsaybulin.model.common.EntryType

const val HOME_ROUTE = "home_route"

fun NavController.navigateToHome(navOptions: NavOptions) {
    navigate(HOME_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onEntryClick: (EntryType, Long) -> Unit,
    onSearchClick: (SearchArgs) -> Unit,
    onAllNewsTopicsClick: () -> Unit
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(
            onEntryClick = onEntryClick,
            onSearchClick = onSearchClick,
            onAllNewsTopicsClick = onAllNewsTopicsClick
        )
    }
}
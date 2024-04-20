package ru.vladsaybulin.feature.details.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.vladsaybulin.core.navigation.args.EntryDetailsArgs
import ru.vladsaybulin.core.navigation.args.ImageViewArgs
import ru.vladsaybulin.core.navigation.args.SearchArgs
import ru.vladsaybulin.core.navigation.util.withParentGraphRoute
import ru.vladsaybulin.feature.details.DetailsRoute
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.asEntryType
import ru.vladsaybulin.model.userrate.UserRateWithEntry

const val ENTRY_DETAILS_ROUTE = "details"

private const val ENTRY_ID_ARG = "id"
private const val ENTRY_TYPE_ARG = "type"

fun NavController.navigateToEntryDetails(
    args: EntryDetailsArgs,
    navOptions: NavOptions? = null
) {
    navigate(
        route = "${withParentGraphRoute(ENTRY_DETAILS_ROUTE)}/${args.entryType}/${args.entryId}",
        navOptions = navOptions
    )
}

fun NavGraphBuilder.detailsScreen(
    onEntryClick: (EntryDetailsArgs) -> Unit,
    onSearchClick: (SearchArgs) -> Unit,
    onAuthorClick: (Long) -> Unit,
    onCharacterClick: (Long) -> Unit,
    onShowRequireAuthDialog: () -> Unit,
    onShowUserRate: (UserRateWithEntry) -> Unit,
    onShowImage: (ImageViewArgs) -> Unit,
    onBackClick: () -> Unit,
) {
    composable(
        route = "${withParentGraphRoute(ENTRY_DETAILS_ROUTE)}/{$ENTRY_TYPE_ARG}/{$ENTRY_ID_ARG}",
        arguments = listOf(
            navArgument(ENTRY_TYPE_ARG) { type = NavType.StringType },
            navArgument(ENTRY_ID_ARG) { type = NavType.LongType }
        )
    ) {
        DetailsRoute(
            onEntryClick = onEntryClick,
            onSearchClick = onSearchClick,
            onAuthorClick = onAuthorClick,
            onCharacterClick = onCharacterClick,
            onShowRequireAuthDialog = onShowRequireAuthDialog,
            onShowUserRate = onShowUserRate,
            onShowImage = onShowImage,
            onBackClick = onBackClick
        )
    }
}

internal fun EntryDetailsArgs(savedStateHandle: SavedStateHandle) = EntryDetailsArgs(
    entryType = checkNotNull(savedStateHandle.get<String>(ENTRY_TYPE_ARG)).asEntryType(),
    entryId = checkNotNull(savedStateHandle.get<Long>(ENTRY_ID_ARG))
)
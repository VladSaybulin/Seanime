package ru.vladsaybulin.feature.details.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.vladsaybulin.core.navigation.ImageViewArgs
import ru.vladsaybulin.core.navigation.SearchArgs
import ru.vladsaybulin.feature.details.DetailsRoute
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import ru.vladsaybulin.model.common.asEntryType

private const val ENTRY_ID_ARG = "id"
private const val ENTRY_TYPE_ARG = "type"

const val DETAILS_ROUTE = "details"


internal data class DetailsArgs(val entryType: EntryType, val entryId: Long) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        checkNotNull(savedStateHandle.get<String>(ENTRY_TYPE_ARG)).asEntryType(),
        checkNotNull(savedStateHandle.get<Long>(ENTRY_ID_ARG))
    )
}

fun NavController.navigateToEntryDetails(
    entryType: EntryType,
    entryId: Long
) {
    navigate("$DETAILS_ROUTE/${entryType.serializedName}/$entryId")
}

fun NavGraphBuilder.detailsScreen(
    onEntryClick: (EntryType, Long) -> Unit,
    onSearchClick: (SearchArgs) -> Unit,
    onAuthorClick: (Long) -> Unit,
    onCharacterClick: (Long) -> Unit,
    onShowRequireAuthDialog: () -> Unit,
    onShowUserRate: (UserRateWithEntry) -> Unit,
    onShowImage: (ImageViewArgs) -> Unit,
    onBackClick: () -> Unit,
) {
    composable(
        route = "$DETAILS_ROUTE/{$ENTRY_TYPE_ARG}/{$ENTRY_ID_ARG}",
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
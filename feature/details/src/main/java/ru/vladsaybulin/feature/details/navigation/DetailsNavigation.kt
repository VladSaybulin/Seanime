package ru.vladsaybulin.feature.details.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.vladsaybulin.feature.details.DetailsRoute
import ru.vladsaybulin.feature.userrate.UserRateEditorContext
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Screenshot
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.asEntryType

private const val ENTRY_ID_ARG = "id"
private const val ENTRY_TYPE_ARG = "type"

private const val DETAILS_ROUTE = "details/{$ENTRY_TYPE_ARG}/{$ENTRY_ID_ARG}"

internal data class DetailsArgs(val entryType: EntryType, val entryId: Long) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        checkNotNull(savedStateHandle.get<String>(ENTRY_TYPE_ARG)).asEntryType(),
        checkNotNull(savedStateHandle.get<Long>(ENTRY_ID_ARG))
    )
}

fun NavController.navigateToDetails(
    entryType: EntryType,
    entryId: Long
) {
    navigate("details/${entryType.serializedName}/$entryId")
}

fun NavGraphBuilder.detailsScreen(
    openEntryDetails: (EntryType, Long) -> Unit,
    openScreenshot: (allScreenshots: List<Screenshot>, screenshotIndex: Int) -> Unit,
    openUserRate: (UserRate, UserRateEditorContext) -> Unit,
    onBackClick: () -> Unit
) {
    composable(
        route = DETAILS_ROUTE,
        arguments = listOf(
            navArgument(ENTRY_TYPE_ARG) { type = NavType.StringType },
            navArgument(ENTRY_ID_ARG) { type = NavType.LongType }
        )
    ) {
        DetailsRoute(
            onEntryClick = openEntryDetails,
            onBackClick = onBackClick,
            onScreenshotClick = openScreenshot,
            onEditUserRateClick = openUserRate
        )
    }


}
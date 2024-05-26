package ru.vladsaybulin.feature.authors.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.vladsaybulin.core.navigation.SeanimeNavigator
import ru.vladsaybulin.core.navigation.util.withParentGraphRoute
import ru.vladsaybulin.feature.authors.AuthorsRoute
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.asEntryType

private const val AUTHORS_ROUTE = "authors"

private const val ENTRY_TYPE_ARG = "entry_type"
private const val ENTRY_ID_ARG = "entry_id"

fun NavController.navigateToAuthors(entryType: EntryType, entryId: Long) {
    navigate(withParentGraphRoute("$AUTHORS_ROUTE/${entryType.serializedName}/$entryId"))
}

internal data class AuthorsArgs(
    val entryType: EntryType,
    val entryId: Long
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        checkNotNull(savedStateHandle.get<String>(ENTRY_TYPE_ARG)?.asEntryType()),
        checkNotNull(savedStateHandle.get<Long>(ENTRY_ID_ARG))
    )
}

fun NavGraphBuilder.authorsScreen(
    navigator: SeanimeNavigator
) {
    composable(
        route = withParentGraphRoute("$AUTHORS_ROUTE/{$ENTRY_TYPE_ARG}/{$ENTRY_ID_ARG}"),
        arguments = listOf(
            navArgument(ENTRY_TYPE_ARG) { type = NavType.StringType },
            navArgument(ENTRY_ID_ARG) { type = NavType.LongType }
        )
    ) {
        AuthorsRoute(navigator = navigator)
    }
}
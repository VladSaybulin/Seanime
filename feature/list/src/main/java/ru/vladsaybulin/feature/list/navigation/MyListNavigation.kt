package ru.vladsaybulin.feature.list.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import ru.vladsaybulin.core.navigation.args.EntryDetailsArgs
import ru.vladsaybulin.core.navigation.args.ListArgs
import ru.vladsaybulin.core.navigation.util.appendArg
import ru.vladsaybulin.core.navigation.util.withParentGraphRoute
import ru.vladsaybulin.feature.list.MyListRoute
import ru.vladsaybulin.model.common.asEntryType
import ru.vladsaybulin.model.userrate.asUserRateStatus

const val MY_LIST_GRAPH_ROUTE = "my_list_graph"
private const val LIST_SCREEN_ROUTE = "list_route"

private const val ENTRY_TYPE_ARG = "entry_type"
private const val USER_RATE_STATUS_ARG = "status"

private const val RouteArguments = "$ENTRY_TYPE_ARG={$ENTRY_TYPE_ARG}&" +
        "$USER_RATE_STATUS_ARG={$USER_RATE_STATUS_ARG}"

fun NavController.navigateToMyListGraph(navOptions: NavOptions? = null) {
    navigate(MY_LIST_GRAPH_ROUTE, navOptions)
}

fun NavController.navigateToList(
    args: ListArgs,
    navOptions: NavOptions? = null
) {
    navigate(
        route = "${withParentGraphRoute(LIST_SCREEN_ROUTE)}?${args.encode()}",
        navOptions = navOptions
    )
}

fun NavGraphBuilder.listGraph(
    onEntryClick: (EntryDetailsArgs) -> Unit,
    onSignIn: () -> Unit,
    nested: NavGraphBuilder.() -> Unit
) {
    navigation(
        startDestination = "$MY_LIST_GRAPH_ROUTE/$LIST_SCREEN_ROUTE?$RouteArguments",
        route = MY_LIST_GRAPH_ROUTE
    ) {
        listScreen(onEntryClick, onSignIn)
        nested()
    }
}

fun NavGraphBuilder.listScreen(
    onEntryClick: (EntryDetailsArgs) -> Unit,
    onSignIn: () -> Unit,
) {
    composable(
        route = "${withParentGraphRoute(LIST_SCREEN_ROUTE)}?$RouteArguments",
        arguments = listOf(
            navArgument(ENTRY_TYPE_ARG) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(USER_RATE_STATUS_ARG) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        MyListRoute(
            onEntryClick = onEntryClick,
            onSignIn = onSignIn
        )
    }
}

internal fun ListArgs(savedStateHandle: SavedStateHandle) = ListArgs(
    entryType = savedStateHandle.get<String>(ENTRY_TYPE_ARG)?.asEntryType(),
    userRateStatus = savedStateHandle.get<String>(USER_RATE_STATUS_ARG)?.asUserRateStatus()
)

private fun ListArgs.encode() = buildString {
    entryType?.let { appendArg(ENTRY_TYPE_ARG, it.serializedName) }
    userRateStatus?.let { appendArg(USER_RATE_STATUS_ARG, it.serializedName) }
}
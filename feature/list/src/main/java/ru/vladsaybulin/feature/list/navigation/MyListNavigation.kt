package ru.vladsaybulin.feature.list.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.vladsaybulin.feature.list.MyListRoute
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.model.asEntryType
import ru.vladsaybulin.model.asUserRateStatus

private const val EntryTypeArg = "entry_type"
private const val UserRateStatusArg = "status"

private const val MyListScreenRoute = "my_list"

internal data class MyListArgs(
    val entryType: EntryType?,
    val userRateStatus: UserRateStatus?
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        entryType = savedStateHandle.get<String>(EntryTypeArg)?.asEntryType(),
        userRateStatus = savedStateHandle.get<String>(UserRateStatusArg)?.asUserRateStatus()
    )
}

fun NavController.navigateToMyList(
    entryType: EntryType? = null,
    userRateStatus: UserRateStatus? = null,
    navOptions: NavOptions? = null
) {
    val args = buildString {
        if (entryType != null) {
            if (isNotEmpty()) append("&")
            append(EntryTypeArg).append("=").append(entryType.serializedName)
        }
        if (userRateStatus != null) {
            if (isNotEmpty()) append("&")
            append(EntryTypeArg).append("=").append(userRateStatus.serializedName)
        }
    }
    val route = if (args.isNotEmpty()) "$MyListScreenRoute?$args" else MyListScreenRoute
    navigate(route, navOptions)
}

fun NavGraphBuilder.myListScreen(
    onEntryClick: (type: EntryType, entryId: Long) -> Unit
) {
    composable(
        route = "$MyListScreenRoute?$EntryTypeArg={$EntryTypeArg}&$UserRateStatusArg={$UserRateStatusArg}",
        arguments = listOf(
            navArgument(EntryTypeArg) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument(UserRateStatusArg) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        MyListRoute(
            onEntryClick = onEntryClick
        )
    }
}
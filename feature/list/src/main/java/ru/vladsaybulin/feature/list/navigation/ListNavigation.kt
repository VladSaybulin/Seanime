package ru.vladsaybulin.feature.list.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.list.ListScreen
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus

@Serializable
object ListGraphRoute

@Serializable
internal class ListScreenRoute(
    val userId: Long = MY_ID,
    val titleType: EntryType = EntryType.Anime,
    val status: UserRateStatus = UserRateStatus.Watching,
) {
    companion object {
        const val MY_ID = -1L
    }
}

fun NavController.navigateToListGraph(navOptions: NavOptions?) {
    navigate(ListGraphRoute, navOptions)
}

fun NavController.navigateToListScreen(userId: Long, titleType: EntryType, status: UserRateStatus) {
    navigate(ListScreenRoute(userId, titleType, status))
}

fun NavController.navigateToListScreen(titleType: EntryType, status: UserRateStatus) {
    navigate(ListScreenRoute(ListScreenRoute.MY_ID, titleType, status))
}

fun  NavGraphBuilder.listGraph(
    navEvents: ListNavEvents,
    nested: NavGraphBuilder.() -> Unit
) {
    navigation<ListGraphRoute>(startDestination = ListScreenRoute::class) {
        listScreen(navEvents)
        nested()
    }
}

fun NavGraphBuilder.listScreen(navEvents: ListNavEvents) {
    composable<ListScreenRoute> {
        ListScreen(navEvents)
    }
}
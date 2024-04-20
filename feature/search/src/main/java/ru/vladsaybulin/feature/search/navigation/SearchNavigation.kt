package ru.vladsaybulin.feature.search.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.vladsaybulin.core.navigation.args.EntryDetailsArgs
import ru.vladsaybulin.core.navigation.args.SearchArgs
import ru.vladsaybulin.core.navigation.util.appendArg
import ru.vladsaybulin.core.navigation.util.nullableNavArgument
import ru.vladsaybulin.core.navigation.util.withParentGraphRoute
import ru.vladsaybulin.feature.search.SearchRoute
import ru.vladsaybulin.model.common.asEntryStatus
import ru.vladsaybulin.model.search.SearchType

const val SEARCH_GRAPH_ROUTE = "search_graph"
private const val SEARCH_SCREEN_ROUTE = "search_route"

private const val SEARCH_TYPE_ARG = "type"
private const val ENTRY_STATUS_ARG = "status"
private const val GENRE_ID_ARG = "genre"
private const val DEMOGRAPHIC_ID_ARG = "demographic"
private const val THEME_ID_ARG = "theme"
private const val STUDIO_ID_ARG = "studio"
private const val PUBLISHER_ID_ARG = "publisher"

private const val RouteArguments = "$SEARCH_TYPE_ARG={$SEARCH_TYPE_ARG}&" +
        "$ENTRY_STATUS_ARG={$ENTRY_STATUS_ARG}&" +
        "$GENRE_ID_ARG={$GENRE_ID_ARG}&" +
        "$DEMOGRAPHIC_ID_ARG={$DEMOGRAPHIC_ID_ARG}&" +
        "$THEME_ID_ARG={$THEME_ID_ARG}&" +
        "$STUDIO_ID_ARG={$STUDIO_ID_ARG}&" +
        "$PUBLISHER_ID_ARG={$PUBLISHER_ID_ARG}"

fun NavController.navigateToSearchGraph(navOptions: NavOptions? = null) {
    navigate(SEARCH_GRAPH_ROUTE, navOptions)
}

fun NavController.navigateToSearch(
    args: SearchArgs = SearchArgs(),
    navOptions: NavOptions? = null
) {
    navigate(
        route = "${withParentGraphRoute(SEARCH_SCREEN_ROUTE)}?${args.encode()}",
        navOptions = navOptions
    )
}

fun NavGraphBuilder.searchGraph(
    onEntryClick: (EntryDetailsArgs) -> Unit,
    nested: NavGraphBuilder.() -> Unit,
) {
    navigation(
        startDestination = "$SEARCH_GRAPH_ROUTE/$SEARCH_SCREEN_ROUTE?$RouteArguments",
        route = SEARCH_GRAPH_ROUTE
    ) {
        searchScreen(onEntryClick)
        nested()
    }
}

fun NavGraphBuilder.searchScreen(
    onEntryClick: (EntryDetailsArgs) -> Unit
) {
    composable(
        route = "${withParentGraphRoute(SEARCH_SCREEN_ROUTE)}?$RouteArguments",
        arguments = listOf(
            nullableNavArgument(SEARCH_TYPE_ARG),
            nullableNavArgument(ENTRY_STATUS_ARG),
            nullableNavArgument(GENRE_ID_ARG),
            nullableNavArgument(DEMOGRAPHIC_ID_ARG),
            nullableNavArgument(THEME_ID_ARG),
            nullableNavArgument(STUDIO_ID_ARG),
            nullableNavArgument(PUBLISHER_ID_ARG)
        )
    ) {
        SearchRoute(onEntryClick = onEntryClick)
    }
}

internal fun SearchArgs(savedStateHandle: SavedStateHandle) = SearchArgs(
    searchType = savedStateHandle.get<String>(SEARCH_TYPE_ARG)?.asSearchType(),
    entryStatus = savedStateHandle.get<String>(ENTRY_STATUS_ARG)?.asEntryStatus(),
    genreId = savedStateHandle.get<String>(GENRE_ID_ARG)?.toLong(),
    demographicId = savedStateHandle.get<String>(DEMOGRAPHIC_ID_ARG)?.toLong(),
    themeId = savedStateHandle.get<String>(THEME_ID_ARG)?.toLong(),
    studioId = savedStateHandle.get<String>(STUDIO_ID_ARG)?.toLong(),
    publisherId = savedStateHandle.get<String>(PUBLISHER_ID_ARG)?.toLong()
).apply {
    check(entryStatus == null || searchType != null)
    check(genreId == null || searchType != null)
    check(demographicId == null || searchType != null)
    check(demographicId == null || searchType != null)
    check(studioId == null || searchType == SearchType.Anime)
    check(publisherId == null || searchType == SearchType.Manga)
}

private fun SearchArgs.encode() = buildString {
    searchType?.let { appendArg(SEARCH_TYPE_ARG, it.asString()) }
    entryStatus?.let { appendArg(ENTRY_STATUS_ARG, it.serializedName) }
    genreId?.let { appendArg(GENRE_ID_ARG, it.toString()) }
    themeId?.let { appendArg(THEME_ID_ARG, it.toString()) }
    demographicId?.let { appendArg(DEMOGRAPHIC_ID_ARG, it.toString()) }
    studioId?.let { appendArg(STUDIO_ID_ARG, it.toString()) }
}

private fun String.asSearchType(): SearchType =
    searchTypeToString.entries.first { it.value == this }.key

private fun SearchType.asString(): String =
    checkNotNull(searchTypeToString[this])


private val searchTypeToString = mapOf(
    SearchType.Anime to "anime",
    SearchType.Manga to "manga",
    SearchType.Ranobe to "ranobe"
)
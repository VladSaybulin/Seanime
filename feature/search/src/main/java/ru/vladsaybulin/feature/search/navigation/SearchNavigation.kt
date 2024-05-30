package ru.vladsaybulin.feature.search.navigation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.vladsaybulin.core.navigation.SeanimeNavigator
import ru.vladsaybulin.core.navigation.util.nullableNavArgument
import ru.vladsaybulin.core.navigation.util.withParentGraphRoute
import ru.vladsaybulin.feature.search.SearchRoute
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.asEntryStatus
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.genre.asGenreKind
import ru.vladsaybulin.model.search.SearchType

const val SEARCH_GRAPH_ROUTE = "search_graph"
private const val SEARCH_SCREEN_ROUTE = "search_route"

private const val SEARCH_TYPE_ARG = "type"
private const val ENTRY_STATUS_ARG = "status"
private const val GENRE_KIND_ARG = "genre_kind"
private const val GENRE_ID_ARG = "genre_id"
private const val STUDIO_OR_PUBLISHER_ID_ARG = "studio"

private const val RouteArguments = "$SEARCH_TYPE_ARG={$SEARCH_TYPE_ARG}&" +
        "$ENTRY_STATUS_ARG={$ENTRY_STATUS_ARG}&" +
        "$GENRE_KIND_ARG={$GENRE_ID_ARG}&" +
        "$GENRE_ID_ARG={$GENRE_ID_ARG}&" +
        "$STUDIO_OR_PUBLISHER_ID_ARG={$STUDIO_OR_PUBLISHER_ID_ARG}"

fun NavController.navigateToSearchGraph(navOptions: NavOptions? = null) {
    navigate(SEARCH_GRAPH_ROUTE, navOptions)
}

fun NavController.navigateToSearchByGenre(
    searchType: SearchType,
    genreKind: GenreKind,
    genreId: Long
) {
    navigateToSearch {
        appendQueryParameter(SEARCH_TYPE_ARG, searchType.asString())
        appendQueryParameter(GENRE_KIND_ARG, genreKind.serializedName)
        appendQueryParameter(GENRE_ID_ARG, genreId.toString())
    }
}

fun NavController.navigateToSearchByStudioOrPublisher(
    searchType: SearchType,
    studioOrPublisherId: Long
) {
    navigateToSearch {
        appendQueryParameter(SEARCH_TYPE_ARG, searchType.asString())
        appendQueryParameter(STUDIO_OR_PUBLISHER_ID_ARG, studioOrPublisherId.toString())
    }
}

fun NavController.navigateToSearchOngoingAnimes() {
    navigateToSearch {
        appendQueryParameter(SEARCH_TYPE_ARG, SearchType.Anime.asString())
        appendQueryParameter(ENTRY_STATUS_ARG, EntryStatus.Ongoing.serializedName)
    }
}

private fun NavController.navigateToSearch(builder: Uri.Builder.() -> Unit) =
    navigate(Uri.Builder().apply(builder).build().toString())

internal data class SearchArgs(
    val searchType: SearchType?,
    val entryStatus: EntryStatus?,
    val genreKind: GenreKind?,
    val genreId: String?,
    val studioOrPublisherId: String?
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        savedStateHandle.get<String>(SEARCH_TYPE_ARG)?.asSearchType(),
        savedStateHandle.get<String>(ENTRY_STATUS_ARG)?.asEntryStatus(),
        savedStateHandle.get<String>(GENRE_KIND_ARG)?.asGenreKind(),
        savedStateHandle.get<String>(GENRE_ID_ARG),
        savedStateHandle.get<String>(STUDIO_OR_PUBLISHER_ID_ARG)
    )

    init {
        validate()
    }

    private fun validate() {
        if (entryStatus == null && genreId == null && studioOrPublisherId == null) {
            return
        }

        check(searchType in listOf(SearchType.Anime, SearchType.Manga, SearchType.Ranobe)) {
            "SearchType must be Anime, Manga or Ranobe for filtered search"
        }

        check(genreId == null || genreKind != null) {
            "GenreKind must be specified for search by genre"
        }
    }
}

fun NavGraphBuilder.searchGraph(
    navigator: SeanimeNavigator,
    nested: NavGraphBuilder.() -> Unit,
) {
    navigation(
        startDestination = "$SEARCH_GRAPH_ROUTE/$SEARCH_SCREEN_ROUTE",
        route = SEARCH_GRAPH_ROUTE
    ) {

        composable(route = withParentGraphRoute(SEARCH_SCREEN_ROUTE)) {
            SearchRoute(navigator = navigator)
        }

        nested()
    }
}


fun NavGraphBuilder.searchScreen(navigator: SeanimeNavigator) {
    composable(
        route = "${withParentGraphRoute(SEARCH_SCREEN_ROUTE)}?$RouteArguments",
        arguments = listOf(
            nullableNavArgument(SEARCH_TYPE_ARG),
            nullableNavArgument(ENTRY_STATUS_ARG),
            nullableNavArgument(GENRE_KIND_ARG),
            nullableNavArgument(GENRE_ID_ARG),
            nullableNavArgument(STUDIO_OR_PUBLISHER_ID_ARG)
        )
    ) {
        SearchRoute(navigator = navigator)
    }
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
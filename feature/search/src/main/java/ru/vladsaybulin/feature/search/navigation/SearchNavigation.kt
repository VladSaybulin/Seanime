package ru.vladsaybulin.feature.search.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.vladsaybulin.core.navigation.SearchArgs
import ru.vladsaybulin.core.navigation.util.appendArg
import ru.vladsaybulin.feature.search.SearchRoute
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.asEntryStatus
import ru.vladsaybulin.model.asEntryType

private const val SEARCH_ROUTE = "search"

private const val EntryTypeArg = "type"
private const val EntryStatusArg = "status"
private const val GenreIdArg = "genre"
private const val StudioIdArg = "studio"
private const val PublisherIdArg = "publisher"


internal fun SearchArgs(savedStateHandle: SavedStateHandle): SearchArgs {
    val entryType: EntryType? = savedStateHandle.get<String>(EntryTypeArg)?.asEntryType()
    val entryStatus: EntryStatus? = savedStateHandle.get<String>(EntryStatusArg)?.asEntryStatus()
    check(entryStatus == null || entryType != null)

    val genreId: String? = savedStateHandle[GenreIdArg]
    check(genreId == null || entryType != null)

    val studioId: String? = savedStateHandle[StudioIdArg]
    check(studioId == null || entryType == EntryType.Anime)

    val publishedId: String? = savedStateHandle[PublisherIdArg]
    check(publishedId == null || entryType == EntryType.Manga)

    return SearchArgs(
        entryType = entryType,
        entryStatus = entryStatus,
        genreId = genreId?.toLong(),
        studioId = studioId?.toLong(),
        publisherId = publishedId?.toLong()
    )
}

fun NavController.navigateToSearch(args: SearchArgs = SearchArgs(), navOptions: NavOptions? = null) {
    val encodedArgs = buildString {
        args.entryType?.let { appendArg(EntryTypeArg, it.serializedName) }
        args.entryStatus?.let { appendArg(EntryStatusArg, it.serializedName) }
        args.genreId?.let { appendArg(GenreIdArg, it.toString()) }
        args.studioId?.let { appendArg(GenreIdArg, it.toString()) }
    }
    if (encodedArgs.isNotEmpty()) {
        navigate("$SEARCH_ROUTE?$encodedArgs", navOptions)
    } else {
        navigate(SEARCH_ROUTE, navOptions)
    }
}

fun NavGraphBuilder.searchScreen(
    onEntryClick: (EntryType, Long) -> Unit
) {
    composable(
        route = "$SEARCH_ROUTE?" +
                "$EntryTypeArg={$EntryTypeArg}&" +
                "$EntryStatusArg={$EntryStatusArg}&" +
                "$GenreIdArg={$GenreIdArg}&" +
                "$StudioIdArg={$StudioIdArg}&" +
                "$PublisherIdArg={$PublisherIdArg}",
        arguments = listOf(
            navArgument(EntryTypeArg) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            },
            navArgument(EntryStatusArg) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            },
            navArgument(GenreIdArg) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            },
            navArgument(StudioIdArg) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            },
            navArgument(PublisherIdArg) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            }
        )
    ) {
        SearchRoute(onEntryClick = onEntryClick)
    }
}


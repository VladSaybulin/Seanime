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
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.asEntryStatus
import ru.vladsaybulin.model.search.SearchType

private const val SEARCH_ROUTE = "search"

private const val EntryTypeArg = "type"
private const val EntryStatusArg = "status"
private const val GenreIdArg = "genre"
private const val DemographicIdArg = "demographic"
private const val ThemeIdArg = "theme"
private const val StudioIdArg = "studio"
private const val PublisherIdArg = "publisher"


internal fun SearchArgs(savedStateHandle: SavedStateHandle): SearchArgs {
    val searchType: SearchType? = savedStateHandle.get<String>(EntryTypeArg)?.asSearchType()
    val entryStatus: EntryStatus? = savedStateHandle.get<String>(EntryStatusArg)?.asEntryStatus()
    check(entryStatus == null || searchType != null)

    val genreId: String? = savedStateHandle[GenreIdArg]
    check(genreId == null || searchType != null)

    val demographicId: String? = savedStateHandle[DemographicIdArg]
    check(demographicId == null || searchType != null)

    val themeId: String? = savedStateHandle[ThemeIdArg]
    check(demographicId == null || searchType != null)

    val studioId: String? = savedStateHandle[StudioIdArg]
    check(studioId == null || searchType == SearchType.Anime)

    val publishedId: String? = savedStateHandle[PublisherIdArg]
    check(publishedId == null || searchType == SearchType.Manga)

    return SearchArgs(
        searchType = searchType,
        entryStatus = entryStatus,
        genreId = genreId?.toLong(),
        demographicId = demographicId?.toLong(),
        themeId = themeId?.toLong(),
        studioId = studioId?.toLong(),
        publisherId = publishedId?.toLong()
    )
}

fun NavController.navigateToSearch(args: SearchArgs = SearchArgs(), navOptions: NavOptions? = null) {
    val encodedArgs = buildString {
        args.searchType?.let { appendArg(EntryTypeArg, it.asString()) }
        args.entryStatus?.let { appendArg(EntryStatusArg, it.serializedName) }
        args.genreId?.let { appendArg(GenreIdArg, it.toString()) }
        args.themeId?.let { appendArg(ThemeIdArg, it.toString()) }
        args.demographicId?.let { appendArg(DemographicIdArg, it.toString()) }
        args.studioId?.let { appendArg(StudioIdArg, it.toString()) }
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
                "$DemographicIdArg={$DemographicIdArg}&" +
                "$ThemeIdArg={$ThemeIdArg}&" +
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
            navArgument(DemographicIdArg) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            },
            navArgument(ThemeIdArg) {
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

fun String.asSearchType(): SearchType =
    searchTypeToString.entries.first { it.value == this }.key

fun SearchType.asString(): String =
    checkNotNull(searchTypeToString[this])


private val searchTypeToString = mapOf(
    SearchType.Anime to "anime",
    SearchType.Manga to "manga",
    SearchType.Ranobe to "ranobe"
)
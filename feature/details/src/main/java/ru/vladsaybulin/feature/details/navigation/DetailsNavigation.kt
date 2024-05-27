package ru.vladsaybulin.feature.details.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ru.vladsaybulin.core.navigation.SeanimeNavigator
import ru.vladsaybulin.core.navigation.args.EntryDetailsArgs
import ru.vladsaybulin.core.navigation.navtype.EntryTypeNavType
import ru.vladsaybulin.feature.details.DetailsRoute
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.asEntryType
import kotlin.reflect.typeOf

const val ENTRY_DETAILS_ROUTE = "details"

private const val ENTRY_ID_ARG = "id"
private const val ENTRY_TYPE_ARG = "type"

fun NavController.navigateToAnimeDetails(animeId: Long) {
    navigateToEntryDetails(EntryType.Anime, animeId)
}

fun NavController.navigateToMangaDetails(animeId: Long) {
    navigateToEntryDetails(EntryType.Manga, animeId)
}

@Serializable
internal data class DetailsScreenRoute(val entryType: EntryType, val entryId: Long)

private val TypeMap = mapOf(typeOf<EntryType>() to EntryTypeNavType)

internal fun SavedStateHandle.toDetailsScreenRoute() =
    toRoute<DetailsScreenRoute>(TypeMap)

internal fun NavController.navigateToEntryDetails(
    entryType: EntryType,
    entryId: Long,
    navOptions: NavOptions? = null
) {
    navigate(DetailsScreenRoute(entryType, entryId), navOptions)
//    navigate(
//        route = "${withParentGraphRoute(ENTRY_DETAILS_ROUTE)}/${entryType.serializedName}/$entryId",
//        navOptions = navOptions
//    )
}

fun NavGraphBuilder.detailsScreen(navigator: SeanimeNavigator) {
    composable<DetailsScreenRoute>(typeMap = TypeMap) {
        DetailsRoute(navigator = navigator)
    }
}

internal fun EntryDetailsArgs(savedStateHandle: SavedStateHandle) = EntryDetailsArgs(
    entryType = checkNotNull(savedStateHandle.get<String>(ENTRY_TYPE_ARG)).asEntryType(),
    entryId = checkNotNull(savedStateHandle.get<Long>(ENTRY_ID_ARG))
)
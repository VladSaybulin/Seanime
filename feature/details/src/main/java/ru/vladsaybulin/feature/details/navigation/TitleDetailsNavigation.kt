package ru.vladsaybulin.feature.details.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.details.TitleDetailsScreen
import ru.vladsaybulin.model.common.EntryType

@Serializable
internal data class TitleDetailsScreenRoute(
    val titleType: EntryType,
    val titleId: Long
)

fun NavController.navigateToTitleDetails(titleType: EntryType, titleId: Long) {
    navigate(TitleDetailsScreenRoute(titleType, titleId))
}

fun NavGraphBuilder.titleDetailsScreen(navEvents: TitleDetailsNavEvents) {
    composable<TitleDetailsScreenRoute> {
        TitleDetailsScreen(navEvents = navEvents)
    }
}
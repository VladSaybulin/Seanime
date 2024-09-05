package ru.vladsaybulin.feature.title.related.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.title.related.TitleRelatedRoute
import ru.vladsaybulin.model.common.EntryType

@Serializable
internal class TitleRelatedScreenRoute(
    val titleType: EntryType,
    val titleId: Long
)

fun NavController.navigateToTitleRelated(titleType: EntryType, titleId: Long) {
    navigate(TitleRelatedScreenRoute(titleType, titleId))
}

fun NavGraphBuilder.titleRelatedScreen(navEvents: TitleRelatedNavEvents) {
    composable<TitleRelatedScreenRoute> {
        TitleRelatedRoute(navEvents = navEvents)
    }
}

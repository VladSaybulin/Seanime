package ru.vladsaybulin.feature.authors.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.authors.AuthorsScreen
import ru.vladsaybulin.model.common.EntryType

@Serializable
internal class TitleAuthorsScreenRoute(
    val titleType: EntryType,
    val titleId: Long
)

fun NavController.navigateToTitleAuthors(titleType: EntryType, titleId: Long) {
    navigate(TitleAuthorsScreenRoute(titleType, titleId))
}

fun NavGraphBuilder.titleAuthorsScreen(navEvents: TitleAuthorsNavEvents) {
    composable<TitleAuthorsScreenRoute> {
        AuthorsScreen(navEvents = navEvents)
    }
}
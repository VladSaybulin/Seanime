package ru.vladsaybulin.feature.title.characters.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.title.characters.TitleCharactersRoute
import ru.vladsaybulin.model.common.EntryType

@Serializable
internal class TitleCharactersScreenRoute(
    val titleType: EntryType,
    val titleId: Long
)

fun NavController.navigateToTitleCharacters(titleType: EntryType, titleId: Long) {
    navigate(TitleCharactersScreenRoute(titleType, titleId))
}

fun NavGraphBuilder.titleCharactersScreen(navEvents: TitleCharactersNavEvents) {
    composable<TitleCharactersScreenRoute> {
        TitleCharactersRoute(navEvents)
    }
}
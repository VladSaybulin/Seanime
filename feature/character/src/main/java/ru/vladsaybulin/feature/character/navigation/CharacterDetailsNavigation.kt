package ru.vladsaybulin.feature.character.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.character.CharacterDetailsScreen

@Serializable
internal data class CharacterDetailsScreenRoute(val characterId: Long)

fun NavController.navigateToCharacterDetails(characterId: Long) {
    navigate(CharacterDetailsScreenRoute(characterId))
}

fun NavGraphBuilder.characterDetailsScreen(navEvents: CharacterDetailsNavEvents) {
    composable<CharacterDetailsScreenRoute> {
        CharacterDetailsScreen(navEvents = navEvents)
    }
}
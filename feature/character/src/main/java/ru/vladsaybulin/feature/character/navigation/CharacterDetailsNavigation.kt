package ru.vladsaybulin.feature.character.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.vladsaybulin.core.navigation.NavigationEvent
import ru.vladsaybulin.core.navigation.SeanimeNavigator
import ru.vladsaybulin.core.navigation.util.withParentGraphRoute
import ru.vladsaybulin.feature.character.CharacterDetailsRoute

private const val CHARACTER_DETAILS_ROUTE = "character"

private const val CHARACTER_ID_ARG = "character_id"

fun NavController.navigateToCharacter(characterId: Long, navOptions: NavOptions? = null) {
    navigate(withParentGraphRoute("$CHARACTER_DETAILS_ROUTE/$characterId"), navOptions)
}

fun NavGraphBuilder.characterDetailsScreen(
    navigator: SeanimeNavigator
) {
    composable(
        route = withParentGraphRoute("$CHARACTER_DETAILS_ROUTE/{$CHARACTER_ID_ARG}"),
        arguments = listOf(
            navArgument(CHARACTER_ID_ARG) { type = NavType.LongType }
        )
    ) {
        CharacterDetailsRoute(navigator = navigator)
    }
}
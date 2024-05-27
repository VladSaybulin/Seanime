package ru.vladsaybulin.feature.character.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import ru.vladsaybulin.feature.character.CharacterDetailsScreen

@OptIn(InternalSerializationApi::class)
inline fun <reified Route : CharacterDetailsScreenRoute> NavGraphBuilder.characterDetailsScreen(
    navEvents: CharacterDetailsNavEvents
) {
    check(Route::class.serializer() is CharacterDetailsScreenRouteSerializer)

    composable<Route> {
        CharacterDetailsScreen(navEvents = navEvents)
    }
}
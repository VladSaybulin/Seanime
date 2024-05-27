package ru.vladsaybulin.feature.details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import ru.vladsaybulin.feature.details.TitleDetailsScreen

@OptIn(InternalSerializationApi::class)
inline fun <reified Route : TitleDetailsScreenRoute> NavGraphBuilder.titleDetailsScreen(
    navEvents: TitleDetailsNavEvents
) {
    check(Route::class.serializer() is TitleDetailsScreenRouteSerializer)

    composable<Route>(TitleDetailsNavType) {
        TitleDetailsScreen(navEvents = navEvents)
    }
}
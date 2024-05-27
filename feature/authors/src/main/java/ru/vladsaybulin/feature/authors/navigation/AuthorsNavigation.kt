package ru.vladsaybulin.feature.authors.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import ru.vladsaybulin.feature.authors.AuthorsScreen

@OptIn(InternalSerializationApi::class)
inline fun <reified Route : TitleAuthorsScreenRoute> NavGraphBuilder.titleAuthorsScreen(
    navEvents: TitleAuthorsNavEvents
) {
    check(Route::class.serializer() is TitleAuthorsScreenRouteSerializer)

    composable<Route>(TitleAuthorsArgsNavType) {
        AuthorsScreen(navEvents = navEvents)
    }
}
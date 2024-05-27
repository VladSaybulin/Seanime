package ru.vladsaybulin.feature.list.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import ru.vladsaybulin.feature.list.ListScreen

@OptIn(InternalSerializationApi::class)
inline fun <reified Route : ListScreenRoute> NavGraphBuilder.listScreen(navEvents: ListNavEvents) {
    check(Route::class.serializer() is ListScreenRouteSerializer)

    composable<Route>(ListArgsNavType) {
        ListScreen(navEvents)
    }
}
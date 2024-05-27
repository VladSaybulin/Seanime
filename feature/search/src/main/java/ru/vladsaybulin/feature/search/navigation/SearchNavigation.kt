package ru.vladsaybulin.feature.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.vladsaybulin.feature.search.SearchScreen

inline fun <reified Route : SearchScreenRoute> NavGraphBuilder.searchScreen(
    navEvents: SearchNavEvents
) {
    composable<Route>(SearchArgsNavType) {
        SearchScreen(navEvents = navEvents)
    }
}
package ru.vladsaybulin.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.home.HomeScreen

inline fun <reified Route : Any> NavGraphBuilder.homeScreen(navEvents: HomeNavEvents) {
    composable<Route> {
        HomeScreen(navEvents)
    }
}
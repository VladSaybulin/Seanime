package ru.vladsaybulin.feature.title.screenshots.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.title.screenshots.AnimeScreenshotsRoute

@Serializable
internal class AnimeScreenshotsScreenRoute(val animeId: Long)

fun NavController.navigateToAnimeScreenshots(animeId: Long) {
    navigate(AnimeScreenshotsScreenRoute(animeId))
}

fun NavGraphBuilder.animeScreenshotsScreen(navEvents: AnimeScreenshotsNavEvents) {
    composable<AnimeScreenshotsScreenRoute> {
        AnimeScreenshotsRoute(navEvents)
    }
}
package ru.vladsaybulin.feature.title.videos.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.title.videos.AnimeVideosRoute

@Serializable
internal class AnimeVideosScreenRoute(val animeId: Long)

fun NavController.navigateToAnimeVideos(animeId: Long) {
    navigate(AnimeVideosScreenRoute(animeId))
}

fun NavGraphBuilder.animeVideosScreen(navEvents: AnimeVideosNavEvents) {
    composable<AnimeVideosScreenRoute> {
        AnimeVideosRoute(navEvents)
    }
}
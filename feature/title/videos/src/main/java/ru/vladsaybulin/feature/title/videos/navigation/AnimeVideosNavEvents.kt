package ru.vladsaybulin.feature.title.videos.navigation

import ru.vladsaybulin.model.anime.VideoKind

class AnimeVideosNavEvents(
    val navigateToVideo: (url: String, name: String?, kind: VideoKind) -> Unit,
    val navigateUp: () -> Unit
)

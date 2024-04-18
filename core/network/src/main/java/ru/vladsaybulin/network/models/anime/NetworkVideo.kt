package ru.vladsaybulin.network.models.anime

import ru.vladsaybulin.model.anime.VideoKind

class NetworkVideo(
    val name: String?,
    val previewImageUrl: String,
    val videoUrl: String,
    val playerUrl: String,
    val kind: VideoKind
)
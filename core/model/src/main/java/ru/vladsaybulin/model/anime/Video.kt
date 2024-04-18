package ru.vladsaybulin.model.anime

data class Video(
    val name: String?,
    val previewImageUrl: String,
    val videoUrl: String,
    val playerUrl: String,
    val kind: VideoKind
)

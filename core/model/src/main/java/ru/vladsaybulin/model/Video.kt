package ru.vladsaybulin.model

data class Video(
    val previewImageUrl: String,
    val videoUrl: String,
    val playerUrl: String,
    val kind: VideoKind
)

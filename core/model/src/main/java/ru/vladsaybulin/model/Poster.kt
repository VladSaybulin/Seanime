package ru.vladsaybulin.model

data class Poster(
    val originalUrl: String,
    val previewUrl: String = originalUrl
)
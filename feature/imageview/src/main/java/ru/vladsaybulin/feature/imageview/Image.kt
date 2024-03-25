package ru.vladsaybulin.feature.imageview

data class Image(
    val originalUrl: String,
    val cachedUrl: String = originalUrl
)
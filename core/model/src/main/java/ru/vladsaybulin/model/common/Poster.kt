package ru.vladsaybulin.model.common

data class Poster(
    override val originalUrl: String,
    override val previewUrl: String = originalUrl
) : Image
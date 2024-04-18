package ru.vladsaybulin.model

import ru.vladsaybulin.model.common.Image

data class Poster(
    override val originalUrl: String,
    override val previewUrl: String = originalUrl
) : Image
package ru.vladsaybulin.model.anime

import ru.vladsaybulin.model.common.Image

data class Screenshot(
    val x166Url: String,
    val x332Url: String,
    override val originalUrl: String
) : Image {
    override val previewUrl: String = x332Url
}
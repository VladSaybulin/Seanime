package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.PosterDbo
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.network.models.PosterDto

fun PosterDto.asDbo() = PosterDbo(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)

fun PosterDto.asPoster() = Poster(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)

fun Poster.asDbo() = PosterDbo(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)

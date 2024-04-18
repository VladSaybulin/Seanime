package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.common.ImageEntity
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.network.models.PosterDto

fun PosterDto.asEntity() = ImageEntity(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)

fun PosterDto.asPoster() = Poster(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)

fun Poster.asEntity() = ImageEntity(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)

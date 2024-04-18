package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.common.ImagePOJO
import ru.vladsaybulin.model.common.Poster
import ru.vladsaybulin.network.models.PosterDto

fun PosterDto.asEntity() = ImagePOJO(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)

fun PosterDto.asPoster() = Poster(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)

fun Poster.asEntity() = ImagePOJO(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)

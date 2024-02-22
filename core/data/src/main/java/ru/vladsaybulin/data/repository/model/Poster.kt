package ru.vladsaybulin.data.repository.model

import ru.vladsaybulin.database.models.PosterDbo
import ru.vladsaybulin.network.retrofit.models.PosterDto

fun PosterDto.asDbo() = PosterDbo(originalUrl, previewUrl)

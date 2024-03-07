package ru.vladsaybulin.data.repository.model

import ru.vladsaybulin.database.models.PosterDbo
import ru.vladsaybulin.network.models.PosterDto

fun PosterDto.asDbo() = PosterDbo("https://shikimori.one/$originalUrl", "https://shikimori.one/$previewUrl")

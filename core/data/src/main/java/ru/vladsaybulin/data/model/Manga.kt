package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.MangaDbo
import ru.vladsaybulin.model.Manga

fun Manga.asDbo() = MangaDbo(
    id = id,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asDbo(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asDbo(),
    releasedOn = releasedOn?.asDbo()
)
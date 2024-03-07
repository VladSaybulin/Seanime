package ru.vladsaybulin.feature.details.model

import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Poster

data class SimilarEntry(
    val id: Long,
    val entryType: EntryType,
    val originalName: String,
    val russianName: String?,
    val poster: Poster?
)

fun Anime.asSimilarEntry() = SimilarEntry(
    id = id,
    entryType = EntryType.Anime,
    originalName = originalName,
    russianName = russianName,
    poster = poster
)
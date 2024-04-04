package ru.vladsaybulin.data.model

import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.SimilarEntry
import ru.vladsaybulin.network.models.AnimeDto
import ru.vladsaybulin.network.models.MangaDto

fun AnimeDto.asSimilarEntry() = SimilarEntry(
    entryId = id,
    entryType = EntryType.Anime,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asPoster()
)

fun MangaDto.asSimilarEntry() = SimilarEntry(
    entryId = id,
    entryType = EntryType.Manga,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asPoster()
)
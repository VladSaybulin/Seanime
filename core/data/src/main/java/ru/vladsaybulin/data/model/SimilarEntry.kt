package ru.vladsaybulin.data.model

import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.SimilarEntry
import ru.vladsaybulin.network.models.AnimeDto

fun AnimeDto.asSimilarEntry() = SimilarEntry(
    entryId = id,
    entryType = EntryType.Anime,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asPoster()
)
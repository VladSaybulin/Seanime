package ru.vladsaybulin.data.model

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.SimilarEntry
import ru.vladsaybulin.network.models.NetworkAnime
import ru.vladsaybulin.network.models.NetworkManga

fun NetworkAnime.asSimilarEntry() = SimilarEntry(
    entryId = id,
    entryType = EntryType.Anime,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asPoster()
)

fun NetworkManga.asSimilarEntry() = SimilarEntry(
    entryId = id,
    entryType = EntryType.Manga,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asPoster()
)
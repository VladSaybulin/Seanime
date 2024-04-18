package ru.vladsaybulin.model

import ru.vladsaybulin.model.anime.AnimeDetails
import ru.vladsaybulin.model.manga.MangaDetails
import ru.vladsaybulin.model.userrate.UserRate

data class EntryDetails(
    val anime: AnimeDetails? = null,
    val manga: MangaDetails? = null,
    val similarEntries: List<SimilarEntry>,
    val userRate: UserRate?
)
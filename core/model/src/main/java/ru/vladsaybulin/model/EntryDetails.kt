package ru.vladsaybulin.model

data class EntryDetails(
    val anime: AnimeDetails? = null,
    val manga: MangaDetails? = null,
    val similarEntries: List<SimilarEntry>,
)
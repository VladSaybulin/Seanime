package ru.vladsaybulin.model

data class EntryDetails(
    val anime: AnimeDetails?,
    val similarEntries: List<SimilarEntry>,
)
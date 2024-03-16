package ru.vladsaybulin.model

data class EntryDetails(
    val anime: AnimeDetails?,
    val userRate: UserRate?,
    val similarEntries: List<SimilarEntry>,
)
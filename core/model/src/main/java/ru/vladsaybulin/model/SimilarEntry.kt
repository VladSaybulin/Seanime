package ru.vladsaybulin.model

data class SimilarEntry(
    val entryId: Long,
    val entryType: EntryType,
    val originalName: String,
    val russianName: String?,
    val poster: Poster?,
)
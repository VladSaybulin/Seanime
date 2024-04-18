package ru.vladsaybulin.model

data class Genre(
    val id: Long,
    val englishName: String,
    val russianName: String?,
    val entryType: EntryType,
    val kind: GenreKind
)
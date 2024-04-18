package ru.vladsaybulin.model.genre

import ru.vladsaybulin.model.common.EntryType

data class Genre(
    val id: Long,
    val englishName: String,
    val russianName: String?,
    val entryType: EntryType,
    val kind: GenreKind
)
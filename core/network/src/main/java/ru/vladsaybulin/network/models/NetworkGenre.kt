package ru.vladsaybulin.network.models

import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.GenreKind

data class NetworkGenre(
    val id: Long,
    val name: String,
    val russianName: String?,
    val kind: GenreKind,
    val entryType: EntryType
)
package ru.vladsaybulin.model

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Poster

data class SimilarEntry(
    val entryId: Long,
    val entryType: EntryType,
    val originalName: String,
    val russianName: String?,
    val poster: Poster?,
)
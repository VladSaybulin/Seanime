package ru.vladsaybulin.model.manga

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate

class Manga(
    val id: Long,
    val name: String,
    val russianName: String?,
    val poster: Image?,
    val kind: MangaKind,
    val status: EntryStatus,
    val score: Float,
    val chapters: Int,
    val volumes: Int,
    val airedOn: IncompleteDate?,
    val releasedOn: IncompleteDate?
)

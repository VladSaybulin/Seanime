package ru.vladsaybulin.model

class Manga(
    val id: Long,
    val originalName: String,
    val russianName: String?,
    val poster: Poster?,
    val kind: MangaKind,
    val status: EntryStatus,
    val score: Float?,
    val chapters: Int,
    val volumes: Int,
    val airedOn: IncompleteDate?,
    val releasedOn: IncompleteDate?
)

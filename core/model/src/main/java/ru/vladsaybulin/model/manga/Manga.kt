package ru.vladsaybulin.model.manga

import ru.vladsaybulin.model.Entry
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.common.Image

class Manga(
    override val id: Long,
    override val name: String,
    override val russianName: String?,
    override val poster: Image?,
    val kind: MangaKind,
    val status: EntryStatus,
    val score: Float?,
    val chapters: Int,
    val volumes: Int,
    val airedOn: IncompleteDate?,
    val releasedOn: IncompleteDate?
) : Entry {
    override val type = EntryType.Manga
}

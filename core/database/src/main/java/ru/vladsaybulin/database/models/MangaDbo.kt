package ru.vladsaybulin.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.MangaKind

@Entity(tableName = "mangas")
class MangaDbo(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("name") val originalName: String,
    @ColumnInfo("russian_name") val russianName: String?,
    @Embedded("image") val poster: PosterDbo?,
    @ColumnInfo("kind") val kind: MangaKind,
    @ColumnInfo("status") val status: EntryStatus,
    @ColumnInfo("score") val score: Float,
    @ColumnInfo("chapters") val chapters: Int,
    @ColumnInfo("volumes") val volumes: Int,
    @Embedded("aired_on_") val airedOn: IncompleteDateDbo?,
    @Embedded("released_on") val releasedOn: IncompleteDateDbo?
)
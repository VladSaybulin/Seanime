package ru.vladsaybulin.database.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.EntryStatus

@Entity(tableName = "animes")
data class AnimeDbo(
    @ColumnInfo("id") val id: Long,
    @ColumnInfo("name") val originalName: String,
    @ColumnInfo("russian_name") val russianName: String,
    @Embedded("image") val poster: PosterDbo?,
    @ColumnInfo("kind") val kind: AnimeKind,
    @ColumnInfo("status") val status: EntryStatus,
    @ColumnInfo("score") val score: Float,
    @ColumnInfo("episodes") val episodes: Int,
    @ColumnInfo("episodes_aired") val episodesAired: Int,
    @Embedded("aired_on_") val airedOn: IncompleteDateDbo?,
    @Embedded("released_on") val releasedOn: IncompleteDateDbo?
)
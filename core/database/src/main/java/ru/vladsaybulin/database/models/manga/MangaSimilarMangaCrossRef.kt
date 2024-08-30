package ru.vladsaybulin.database.models.manga

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "manga_similar_manga",
    foreignKeys = [
        ForeignKey(
            entity = MangaEntity::class,
            parentColumns = ["id"],
            childColumns = ["manga_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MangaEntity::class,
            parentColumns = ["id"],
            childColumns = ["similar_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["manga_id", "similar_id"]
)
class MangaSimilarMangaCrossRef(
    @ColumnInfo("manga_id")
    val mangaId: Long,

    @ColumnInfo("similar_id")
    val similarMangaId: Long
)
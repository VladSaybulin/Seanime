package ru.vladsaybulin.database.models.anime

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "anime_similar_anime",
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["similar_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["anime_id", "similar_id"]
)
class AnimeSimilarAnimeCrossRef(

    @ColumnInfo("anime_id")
    val animeId: Long,

    @ColumnInfo("similar_id")
    val similarAnimeId: Long
)
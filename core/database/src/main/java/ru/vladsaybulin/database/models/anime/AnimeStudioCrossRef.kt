package ru.vladsaybulin.database.models.anime

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "anime_studio",
    primaryKeys = ["anime_id", "studio_id"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = StudioEntity::class,
            parentColumns = ["id"],
            childColumns = ["studio_id"]
        )
    ]
)
class AnimeStudioCrossRef(

    @ColumnInfo("anime_id")
    val animeId: Long,

    @ColumnInfo("studio_id")
    val studioId: Long,
)
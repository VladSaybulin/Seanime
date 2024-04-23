package ru.vladsaybulin.database.models.anime

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.vladsaybulin.model.common.Image


@Entity(
    tableName = "anime_screenshots",
    primaryKeys = ["anime_id", "order"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AnimeScreenshotEntity(

    @ColumnInfo("anime_id")
    val animeId: Long,

    @ColumnInfo("order")
    val order: Int,

    @ColumnInfo("preview")
    val previewUrl: String,

    @ColumnInfo("original")
    val originalUrl: String
)

fun AnimeScreenshotEntity.asExternalModel() = Image(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)
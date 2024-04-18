package ru.vladsaybulin.database.models.common

import androidx.room.ColumnInfo
import ru.vladsaybulin.model.Poster

data class ImageEntity(

    @ColumnInfo("original")
    val originalUrl: String,

    @ColumnInfo("preview")
    val previewUrl: String
)

fun ImageEntity.asExternalModel() = Poster(originalUrl, previewUrl)
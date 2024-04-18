package ru.vladsaybulin.database.models.common

import androidx.room.ColumnInfo
import ru.vladsaybulin.model.common.Poster

data class ImagePOJO(

    @ColumnInfo("original")
    val originalUrl: String,

    @ColumnInfo("preview")
    val previewUrl: String
)

fun ImagePOJO.asExternalModel() = Poster(originalUrl, previewUrl)
package ru.vladsaybulin.database.models.common

import androidx.room.ColumnInfo
import ru.vladsaybulin.model.common.Image

data class ImagePOJO(

    @ColumnInfo("original")
    val originalUrl: String,

    @ColumnInfo("preview")
    val previewUrl: String
)

fun ImagePOJO.asExternalModel() = Image(originalUrl, previewUrl)
package ru.vladsaybulin.database.models

import androidx.room.ColumnInfo
import ru.vladsaybulin.model.Poster

data class PosterDbo(
    @ColumnInfo("original") val originalUrl: String,
    @ColumnInfo("preview") val previewUrl: String
)

fun PosterDbo.asExternalModel() = Poster(originalUrl, previewUrl)
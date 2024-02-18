package ru.vladsaybulin.database.models

import androidx.room.ColumnInfo

data class PosterDbo(
    @ColumnInfo("original") val originalUrl: String,
    @ColumnInfo("preview") val previewUrl: String
)
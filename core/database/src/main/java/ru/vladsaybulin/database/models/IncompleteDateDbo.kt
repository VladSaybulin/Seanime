package ru.vladsaybulin.database.models

import androidx.room.ColumnInfo


data class IncompleteDateDbo(
    @ColumnInfo("day") val day: Int,
    @ColumnInfo("month") val month: Int,
    @ColumnInfo("year") val year: Int
)

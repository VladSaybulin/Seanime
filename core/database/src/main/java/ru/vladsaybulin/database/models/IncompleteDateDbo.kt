package ru.vladsaybulin.database.models

import androidx.room.ColumnInfo
import ru.vladsaybulin.model.IncompleteDate


data class IncompleteDateDbo(
    @ColumnInfo("day") val day: Int?,
    @ColumnInfo("month") val month: Int?,
    @ColumnInfo("year") val year: Int?
)

fun IncompleteDateDbo.asExternalModel() = IncompleteDate(day, month, year)
package ru.vladsaybulin.database.models.common

import androidx.room.ColumnInfo
import ru.vladsaybulin.model.IncompleteDate


data class IncompleteDateEntity(
    @ColumnInfo("day")
    val day: Int?,

    @ColumnInfo("month")
    val month: Int?,

    @ColumnInfo("year")
    val year: Int?
)

fun IncompleteDateEntity.asExternalModel() = IncompleteDate(day, month, year)
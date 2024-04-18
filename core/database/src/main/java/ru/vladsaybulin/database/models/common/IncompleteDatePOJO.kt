package ru.vladsaybulin.database.models.common

import androidx.room.ColumnInfo
import ru.vladsaybulin.model.common.IncompleteDate


data class IncompleteDatePOJO(

    @ColumnInfo("day")
    val day: Int?,

    @ColumnInfo("month")
    val month: Int?,

    @ColumnInfo("year")
    val year: Int?
)

fun IncompleteDatePOJO.asExternalModel() = IncompleteDate(day, month, year)
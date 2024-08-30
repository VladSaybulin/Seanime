package ru.vladsaybulin.database.models.common

import androidx.room.ColumnInfo
import ru.vladsaybulin.model.search.TimePeriodAiring
import ru.vladsaybulin.model.search.SeasonOfYear

class SeasonPOJO(
    @ColumnInfo("season_of_year")
    val seasonOfYear: SeasonOfYear,

    @ColumnInfo("year")
    val year: Int
)

fun SeasonPOJO.asExternalModel() = TimePeriodAiring.Season(
    seasonOfYear = seasonOfYear,
    year = year
)
package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.common.StatisticsItem

class IntStatisticsItemsConverter {

    @TypeConverter
    fun statisticsItemsToString(value: List<StatisticsItem<Int>>) = ""

    @TypeConverter
    fun stringToStatisticsItems(value: String) = emptyList<StatisticsItem<Int>>()

}
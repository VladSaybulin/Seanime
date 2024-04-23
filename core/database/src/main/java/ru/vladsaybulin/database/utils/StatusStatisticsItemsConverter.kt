package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.userrate.UserRateStatus

class StatusStatisticsItemsConverter {

    @TypeConverter
    fun statisticsItemsToString(value: List<StatisticsItem<UserRateStatus>>) = ""

    @TypeConverter
    fun stringToStatisticsItems(value: String) = emptyList<StatisticsItem<UserRateStatus>>()


}
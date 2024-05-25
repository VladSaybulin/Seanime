package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.database.models.lastrequest.LastRequestType

class LastRequestTypeTypeConverter {

    @TypeConverter
    fun lastRequestTypeToString(value: LastRequestType) = when (value) {
        LastRequestType.ANIME -> "anime"
        LastRequestType.MANGA -> "manga"
        LastRequestType.CHARACTER -> "character"
    }

    @TypeConverter
    fun stringToLastRequestType(value: String) = when (value) {
        "anime" -> LastRequestType.ANIME
        "manga" -> LastRequestType.MANGA
        "character" -> LastRequestType.CHARACTER
        else -> throw IllegalArgumentException("Unknown LastRequestType for $value")
    }

}
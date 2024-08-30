package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.request.Request

class LastRequestTypeTypeConverter {

    @TypeConverter
    fun lastRequestTypeToString(value: Request) = when (value) {
        Request.ANIME -> "anime"
        Request.MANGA -> "manga"
        Request.CHARACTER -> "character"
    }

    @TypeConverter
    fun stringToLastRequestType(value: String) = when (value) {
        "anime" -> Request.ANIME
        "manga" -> Request.MANGA
        "character" -> Request.CHARACTER
        else -> throw IllegalArgumentException("Unknown LastRequestType for $value")
    }

}
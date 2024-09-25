package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.request.Request

class LastRequestTypeTypeConverter {

    @TypeConverter
    fun lastRequestTypeToString(value: Request) = when (value) {
        Request.ANIME -> "anime"
        Request.MANGA -> "manga"
        Request.CHARACTER -> "character"
        Request.ANIME_ONGOINGS -> "anime_ongoing"
        Request.NEWS -> "news"
    }

    @TypeConverter
    fun stringToLastRequestType(value: String) = when (value) {
        "anime" -> Request.ANIME
        "manga" -> Request.MANGA
        "character" -> Request.CHARACTER
        "anime_ongoing" -> Request.ANIME_ONGOINGS
        "news" -> Request.NEWS
        else -> throw IllegalArgumentException("Unknown LastRequestType for $value")
    }

}
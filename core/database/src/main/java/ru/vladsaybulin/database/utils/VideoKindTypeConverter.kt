package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.anime.VideoKind

class VideoKindTypeConverter {

    @TypeConverter
    fun videoKindToString(value: VideoKind) = value.serializedName

    @TypeConverter
    fun stringToTypeConverter(value: String) =
        VideoKind.entries.firstOrNull { it.serializedName == value } ?: VideoKind.Other

}
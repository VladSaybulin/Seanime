package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.asAnimeKind

class AnimeKindTypeConverter {

    @TypeConverter
    fun animeKindToString(value: AnimeKind): String = value.serializedName

    @TypeConverter
    fun stringToAnimeKind(value: String): AnimeKind = value.asAnimeKind()

}
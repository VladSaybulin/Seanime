package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.asAnimeKind

class AnimeKindTypeConverter {

    @TypeConverter
    fun animeKindToString(value: AnimeKind): String = value.serializedName

    @TypeConverter
    fun stringToAnimeKind(value: String): AnimeKind = value.asAnimeKind()

}
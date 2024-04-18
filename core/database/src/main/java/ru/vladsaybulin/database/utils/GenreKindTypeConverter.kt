package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.GenreKind
import ru.vladsaybulin.model.asGenreKind

class GenreKindTypeConverter {

    @TypeConverter
    fun genreKindToString(value: GenreKind) = value.serializedName

    @TypeConverter
    fun stringToGenreKind(value: String) = value.asGenreKind()

}
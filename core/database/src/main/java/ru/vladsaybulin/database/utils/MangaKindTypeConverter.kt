package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.MangaKind
import ru.vladsaybulin.model.asMangaKind

class MangaKindTypeConverter {

    @TypeConverter
    fun mangaKindToString(value: MangaKind): String = value.serializedName

    @TypeConverter
    fun stringToMangaKind(value: String): MangaKind = value.asMangaKind()
}
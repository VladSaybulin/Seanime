package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter

class StringListTypeConverter {

    @TypeConverter
    fun stringListToString(value: List<String>) = value.joinToString(separator = ",")

    @TypeConverter
    fun stringToStringList(value: String) = value.split(',')
}
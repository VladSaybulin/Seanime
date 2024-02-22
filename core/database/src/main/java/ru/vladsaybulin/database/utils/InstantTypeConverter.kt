package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class InstantTypeConverter {
    @TypeConverter
    fun instantToLong(value: Instant) = value.toEpochMilliseconds()

    @TypeConverter
    fun longToInstant(value: Long) = Instant.fromEpochMilliseconds(value)

}
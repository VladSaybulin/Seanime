package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.asEntryType

class EntryStatusTypeConverter {

    @TypeConverter
    fun entryStatusToString(value: EntryStatus) = value.serializedName

    @TypeConverter
    fun stringToEntryStatus(value: String) = value.asEntryType()

}
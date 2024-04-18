package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.asEntryType

class EntryStatusTypeConverter {

    @TypeConverter
    fun entryStatusToString(value: EntryStatus) = value.serializedName

    @TypeConverter
    fun stringToEntryStatus(value: String) = value.asEntryType()

}
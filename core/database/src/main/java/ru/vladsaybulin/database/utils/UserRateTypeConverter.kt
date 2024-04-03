package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.model.asUserRateStatus

class UserRateTypeConverter {
    @TypeConverter
    fun userRateStatusToString(value: UserRateStatus) = value.serializedName

    @TypeConverter
    fun stringToUserRateStatus(value: String) = value.asUserRateStatus()
}
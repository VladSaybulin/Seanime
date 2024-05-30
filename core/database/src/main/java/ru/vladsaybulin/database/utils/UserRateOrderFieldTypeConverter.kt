package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.list.UserRateOrderField
import ru.vladsaybulin.model.list.UserRateOrderField.CreatedAt
import ru.vladsaybulin.model.list.UserRateOrderField.UpdatedAt

class UserRateOrderFieldTypeConverter {

    @TypeConverter
    fun userRateOrderFieldToString(value: UserRateOrderField) = when (value) {
        CreatedAt -> "created_at"
        UpdatedAt -> "updated_at"
    }

    @TypeConverter
    fun stringToUserRateOrderField(value: String) = when (value) {
        "created_at" -> CreatedAt
        "updated_at" -> UpdatedAt
        else -> error("Unknown UserRateOrderField with string=$value")
    }
}
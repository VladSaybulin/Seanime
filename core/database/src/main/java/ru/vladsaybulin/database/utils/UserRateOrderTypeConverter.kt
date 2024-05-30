package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrder.Asc
import ru.vladsaybulin.model.list.UserRateOrder.Desc

class UserRateOrderTypeConverter {

    @TypeConverter
    fun userRateOrderToString(value: UserRateOrder) = when (value) {
        Asc -> "asc"
        Desc -> "desc"
    }

    @TypeConverter
    fun stringToUserRateOrder(value: String) = when (value) {
        "asc" -> Asc
        "desc" -> Desc
        else -> error("Unknown UserRateOrder with string=$value")
    }

}
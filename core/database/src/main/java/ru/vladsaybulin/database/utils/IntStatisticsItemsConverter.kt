package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import ru.vladsaybulin.model.common.StatisticsItem

class IntStatisticsItemsConverter {

    private val json = Json

    @TypeConverter
    fun statisticsItemsToString(value: List<StatisticsItem<Int>>) = json.encodeToString(
        JsonArray(
            value.map {
                JsonObject(
                    mapOf(
                        "value" to JsonPrimitive(it.values),
                        "count" to JsonPrimitive(it.count)
                    )
                )
            }
        )
    )

    @TypeConverter
    fun stringToStatisticsItems(value: String) = Json.decodeFromString<JsonArray>(value).let { array ->
        array.map { element ->
            val obj = (element as JsonObject)
            StatisticsItem(
                values = (obj["value"] as JsonPrimitive).int,
                count = (obj["count"] as JsonPrimitive).int,
            )
        }
    }

}
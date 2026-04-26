/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.asUserRateStatus

class StatusStatisticsItemsConverter {

    private val json = Json

    @TypeConverter
    fun statisticsItemsToString(value: List<StatisticsItem<UserRateStatus>>) = json.encodeToString(
        JsonArray(
            value.map {
                JsonObject(
                    mapOf(
                        "value" to JsonPrimitive(it.values.serializedName),
                        "count" to JsonPrimitive(it.count)
                    )
                )
            }
        )
    )

    @TypeConverter
    fun stringToStatisticsItems(value: String) =
        Json.decodeFromString<JsonArray>(value).let { array ->
            array.map { element ->
                val obj = (element as JsonObject)
                StatisticsItem(
                    values = (obj["value"] as JsonPrimitive).content.asUserRateStatus(),
                    count = (obj["count"] as JsonPrimitive).int,
                )
            }
        }


}
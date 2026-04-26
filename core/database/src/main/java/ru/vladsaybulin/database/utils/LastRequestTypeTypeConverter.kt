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
import ru.vladsaybulin.model.request.Request

class LastRequestTypeTypeConverter {

    @TypeConverter
    fun lastRequestTypeToString(value: Request) = when (value) {
        Request.ANIME -> "anime"
        Request.MANGA -> "manga"
        Request.CHARACTER -> "character"
        Request.ANIME_ONGOINGS -> "anime_ongoing"
        Request.NEWS -> "news"
    }

    @TypeConverter
    fun stringToLastRequestType(value: String) = when (value) {
        "anime" -> Request.ANIME
        "manga" -> Request.MANGA
        "character" -> Request.CHARACTER
        "anime_ongoing" -> Request.ANIME_ONGOINGS
        "news" -> Request.NEWS
        else -> throw IllegalArgumentException("Unknown LastRequestType for $value")
    }

}
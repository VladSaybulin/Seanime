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
import ru.vladsaybulin.model.anime.VideoKind

class VideoKindTypeConverter {

    @TypeConverter
    fun videoKindToString(value: VideoKind) = value.serializedName

    @TypeConverter
    fun stringToTypeConverter(value: String) =
        VideoKind.entries.firstOrNull { it.serializedName == value } ?: VideoKind.Other

}
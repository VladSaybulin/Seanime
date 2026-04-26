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
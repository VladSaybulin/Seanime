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

package ru.vladsaybulin.database.models.user

import androidx.room.ColumnInfo
import ru.vladsaybulin.model.user.UserImage

data class UserImagePOJO(
    @ColumnInfo("x160_url") val x160Url: String,
    @ColumnInfo("x148_url") val x148Url: String,
    @ColumnInfo("x80_url") val x80Url: String,
    @ColumnInfo("x64_url") val x64Url: String,
    @ColumnInfo("x48_url") val x48Url: String,
    @ColumnInfo("x32_url") val x32Url: String,
    @ColumnInfo("x16_url") val x16Url: String
)

fun UserImagePOJO.asExternalModel() = UserImage(
    x160Url = x160Url,
    x148Url = x148Url,
    x80Url = x80Url,
    x64Url = x64Url,
    x48Url = x48Url,
    x32Url = x32Url,
    x16Url = x16Url
)
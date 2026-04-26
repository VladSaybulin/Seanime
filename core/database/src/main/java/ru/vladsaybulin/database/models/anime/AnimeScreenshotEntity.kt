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

package ru.vladsaybulin.database.models.anime

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.vladsaybulin.model.common.Image


@Entity(
    tableName = "anime_screenshots",
    primaryKeys = ["anime_id", "order"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AnimeScreenshotEntity(

    @ColumnInfo("anime_id")
    val animeId: Long,

    @ColumnInfo("order")
    val order: Int,

    @ColumnInfo("preview")
    val previewUrl: String,

    @ColumnInfo("original")
    val originalUrl: String
)

fun AnimeScreenshotEntity.asExternalModel() = Image(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)
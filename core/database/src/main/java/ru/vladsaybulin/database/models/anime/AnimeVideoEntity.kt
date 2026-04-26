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
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.anime.VideoKind

@Entity(
    tableName = "anime_videos",
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
data class AnimeVideoEntity(

    @ColumnInfo("anime_id")
    val animeId: Long,

    @ColumnInfo("order")
    val order: Int,

    @ColumnInfo("name")
    val name: String?,

    @ColumnInfo("preview_url")
    val previewImageUrl: String,

    @ColumnInfo("video_url")
    val videoUrl: String,

    @ColumnInfo("player_url")
    val playerUrl: String,

    @ColumnInfo("kind")
    val kind: VideoKind
)

fun AnimeVideoEntity.asExternalModel() = Video(
    name = name,
    previewImageUrl = previewImageUrl,
    videoUrl = videoUrl,
    playerUrl = playerUrl,
    kind = kind
)
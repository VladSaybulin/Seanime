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

package ru.vladsaybulin.database.models.userrate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus

@Entity(
    tableName = "user_rates",
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = MangaEntity::class,
            parentColumns = ["id"],
            childColumns = ["manga_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class UserRateEntity(
    @ColumnInfo("id")
    @PrimaryKey
    val id: Long,
    @ColumnInfo("anime_id") val animeId: Long?,
    @ColumnInfo("manga_id") val mangaId: Long?,
    @ColumnInfo("status") val status: UserRateStatus,
    @ColumnInfo("score") val score: Int,
    @ColumnInfo("episodes") val episodes: Int,
    @ColumnInfo("chapters") val chapters: Int,
    @ColumnInfo("volumes") val volumes: Int,
    @ColumnInfo("rewatches") val rewatches: Int,
    @ColumnInfo("text") val text: String,
    @ColumnInfo("created_at") val createdAt: Instant,
    @ColumnInfo("updated_at") val updatedAt: Instant
)

fun UserRateEntity.asExternalModel() = UserRate(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    status = status,
    score = score,
    episodes = episodes,
    chapters = chapters,
    volumes = volumes,
    rewatches = rewatches,
    text = text
)
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

package ru.vladsaybulin.database.models.manga

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.model.related.RelationType

@Entity(
    tableName = "manga_related",
    foreignKeys = [
        ForeignKey(
            entity = MangaEntity::class,
            parentColumns = ["id"],
            childColumns = ["manga_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["related_anime_id"]
        ),
        ForeignKey(
            entity = MangaEntity::class,
            parentColumns = ["id"],
            childColumns = ["related_manga_id"]
        )
    ]
)
class MangaRelatedEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo("manga_id")
    val mangaId: Long,

    @ColumnInfo("related_anime_id")
    val relatedAnimeId: Long?,

    @ColumnInfo("related_manga_id")
    val relatedMangaId: Long?,

    @ColumnInfo("relation_type")
    val relationType: RelationType,

    @ColumnInfo("order")
    val order: Int
)
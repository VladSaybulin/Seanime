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

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.vladsaybulin.database.models.genre.GenreEntity

data class PopulatedAnimeDetails(

    @Embedded
    val animeDetailsEntity: AnimeDetailsEntity,

    @Relation(
        entity = GenreEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AnimeGenreCrossRef::class,
            parentColumn = "anime_id",
            entityColumn = "genre_id"
        )
    )
    val genres: List<GenreEntity>,

    @Relation(
        entity = StudioEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AnimeStudioCrossRef::class,
            parentColumn = "anime_id",
            entityColumn = "studio_id"
        )
    )
    val studios: List<StudioEntity>,

    @Relation(
        entity = AnimeScreenshotEntity::class,
        parentColumn = "id",
        entityColumn = "anime_id"
    )
    val screenshots: List<AnimeScreenshotEntity>,

    @Relation(
        entity = AnimeVideoEntity::class,
        parentColumn = "id",
        entityColumn = "anime_id"
    )
    val videos: List<AnimeVideoEntity>
)
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

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.vladsaybulin.database.models.genre.GenreEntity

data class PopulatedMangaDetails(
    @Embedded val mangaDetailsEntity: MangaDetailsEntity,

    @Relation(
        entity = GenreEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MangaGenreCrossRef::class,
            parentColumn = "manga_id",
            entityColumn = "genre_id"
        )
    )
    val genres: List<GenreEntity>,

    @Relation(
        entity = PublisherEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = MangaPublisherCrossRef::class,
            parentColumn = "manga_id",
            entityColumn = "publisher_id"
        )
    )
    val publishers: List<PublisherEntity>
)
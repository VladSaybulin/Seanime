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
import androidx.room.Relation
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.model.related.RelatedAnime
import ru.vladsaybulin.model.related.RelatedManga
import ru.vladsaybulin.model.related.RelatedTitle

data class PopulatedMangaRelated(
    @Embedded
    val mangaRelatedEntity: MangaRelatedEntity,

    @Relation(
        entity = AnimeEntity::class,
        parentColumn = "related_anime_id",
        entityColumn = "id"
    )
    val animeEntity: AnimeEntity?,

    @Relation(
        entity = MangaEntity::class,
        parentColumn = "related_manga_id",
        entityColumn = "id"
    )
    val mangaEntity: MangaEntity?
)

fun PopulatedMangaRelated.asExternalModel() = when {
    animeEntity != null -> RelatedAnime(animeEntity.asExternalModel(), mangaRelatedEntity.relationType)
    mangaEntity != null -> RelatedManga(mangaEntity.asExternalModel(), mangaRelatedEntity.relationType)
    else -> error("animeEntity and mangaEntity can't be null at the same time")

}
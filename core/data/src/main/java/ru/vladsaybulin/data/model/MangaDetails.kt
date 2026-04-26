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

package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.manga.MangaDetailsEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.MangaGenreCrossRef
import ru.vladsaybulin.database.models.manga.MangaPublisherCrossRef
import ru.vladsaybulin.database.models.manga.MangaRelatedEntity
import ru.vladsaybulin.database.models.stats.StatsItemProto
import ru.vladsaybulin.database.models.stats.StatsProto
import ru.vladsaybulin.network.models.manga.NetworkMangaDetails

fun NetworkMangaDetails.asMangaDetailsEntity() =
    MangaDetailsEntity(
        id = id,
        nameEn = nameEn,
        nameJp = nameJp,
        altNames = alternativeName,
        licenseNameRu = licenseNameRu,
        description = descriptionHtml?.asSeanimeText()?.asSeanimeTextPOJO(),
        descriptionSource = descriptionSource,
        scoreStats = scoreStats.asDbModel(),
        statusStats = userRateStatusStats.asDbModel()
    )

fun NetworkMangaDetails.asMangaEntity() = MangaEntity(
    id = id,
    originalName = name,
    russianName = nameRu,
    poster = poster?.asPOJO(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asPOJO(),
    releasedOn = releasedOn?.asPOJO()
)

fun NetworkMangaDetails.genreEntityShells() =
    genres?.map { it.asEntity() }

fun NetworkMangaDetails.mangaGenreCrossReferences() =
    genres?.map { MangaGenreCrossRef(mangaId = id, genreId = it.id) }

fun NetworkMangaDetails.relatedAnimeEntityShells() =
    related?.mapNotNull { it.anime?.asEntity() }

fun NetworkMangaDetails.relatedMangaEntityShells() =
    related?.mapNotNull { it.manga?.asEntity() }

fun NetworkMangaDetails.mangaRelatedEntities() =
    related?.mapIndexed { index, it ->
        MangaRelatedEntity(
            mangaId = id,
            relatedMangaId = it.manga?.id,
            relatedAnimeId = it.anime?.id,
            relationType = it.relationType,
            order = index
        )
    }

fun NetworkMangaDetails.publisherEntityShells() =
    publishers.map { it.asEntity() }

fun NetworkMangaDetails.mangaPublisherCrossRefs() =
    publishers.map { MangaPublisherCrossRef(id, it.id) }

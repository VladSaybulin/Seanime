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

import ru.vladsaybulin.database.models.anime.AnimeDetailsEntity
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.AnimeGenreCrossRef
import ru.vladsaybulin.database.models.anime.AnimeRelatedEntity
import ru.vladsaybulin.database.models.anime.AnimeScreenshotEntity
import ru.vladsaybulin.database.models.anime.AnimeStudioCrossRef
import ru.vladsaybulin.database.models.anime.AnimeVideoEntity
import ru.vladsaybulin.database.models.common.SeasonPOJO
import ru.vladsaybulin.database.models.stats.StatsProto
import ru.vladsaybulin.network.models.anime.NetworkAnimeDetails

fun NetworkAnimeDetails.asAnimeDetailsEntity() = AnimeDetailsEntity(
    id = id,
    nameEn = nameEn,
    nameJp = nameJp,
    altNames = alternativeName,
    licenseNameRu = licenseNameRu,
    rating = rating,
    duration = duration ?: 0,
    nextEpisodeAt = nextEpisodeAt,
    description = descriptionHtml?.asSeanimeText()?.asSeanimeTextPOJO(),
    descriptionSource = descriptionSource,
    subbers = subbers,
    dubbers = dubbers,
    scoreStats = scoreStats.asDbModel(),
    statusStats = userRateStatusStats.asDbModel(),
    season = season?.let { SeasonPOJO(it.seasonOfYear, year = it.year) }
)

fun NetworkAnimeDetails.asAnimeEntity() = AnimeEntity(
    id = id,
    originalName = name,
    russianName = nameRu,
    poster = poster?.asPOJO(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.asPOJO(),
    releasedOn = releasedOn?.asPOJO()
)

fun NetworkAnimeDetails.genreEntityShells() =
    genres?.map { it.asEntity() }

fun NetworkAnimeDetails.animeGenresCrossReferences() =
    genres?.map { AnimeGenreCrossRef(animeId = id, genreId = it.id) }

fun NetworkAnimeDetails.animeScreenshotEntityShells() =
    screenshots.mapIndexed { index, image ->
        AnimeScreenshotEntity(
            animeId = id,
            order = index,
            previewUrl = image.previewUrl,
            originalUrl = image.originalUrl
        )
    }

fun NetworkAnimeDetails.animeVideoEntityShells() =
    videos?.mapIndexed { index, video ->
        AnimeVideoEntity(
            animeId = id,
            order = index,
            name = video.name,
            previewImageUrl = video.previewImageUrl,
            videoUrl = video.videoUrl,
            playerUrl = video.playerUrl,
            kind = video.kind
        )
    }

fun NetworkAnimeDetails.relatedAnimeEntityShells() =
    related?.mapNotNull { it.anime?.asEntity() }

fun NetworkAnimeDetails.relatedMangaEntityShells() =
    related?.mapNotNull { it.manga?.asEntity() }

fun NetworkAnimeDetails.animeRelatedEntities() =
    related?.mapIndexed { index, it ->
        AnimeRelatedEntity(
            animeId = id,
            relatedAnimeId = it.anime?.id,
            relatedMangaId = it.manga?.id,
            relationType = it.relationType,
            order = index
        )
    }

fun NetworkAnimeDetails.studioEntityShells() =
    studios.map { it.asEntity() }

fun NetworkAnimeDetails.animeStudioCrossRefs() =
    studios.map { AnimeStudioCrossRef(id, it.id) }
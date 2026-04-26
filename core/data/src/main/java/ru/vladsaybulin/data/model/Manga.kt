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

import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.userrate.UserRateEntity
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.network.models.manga.NetworkManga

fun Manga.asPOJO() = MangaEntity(
    id = id,
    originalName = name,
    russianName = russianName,
    poster = poster?.asPOJO(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asPOJO(),
    releasedOn = releasedOn?.asPOJO()
)

fun NetworkManga.asEntity() = MangaEntity(
    id = id,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asPOJO(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asPOJO(),
    releasedOn = releasedOn?.asPOJO()
)

fun NetworkManga.userRateEntityShell() = userRate?.let { userRate ->
    UserRateEntity(
        id = userRate.id,
        animeId = null,
        mangaId = id,
        status = userRate.status,
        score = userRate.score,
        episodes = 0,
        chapters = chapters,
        volumes = volumes,
        rewatches = userRate.rewatches,
        text = userRate.text ?: "",
        createdAt = userRate.createdAt,
        updatedAt = userRate.updatedAt
    )
}

fun NetworkManga.asExternalModel() = Manga(
    id = id,
    name = originalName,
    russianName = russianName,
    poster = poster?.asExternalModel(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asExternalModel(),
    releasedOn = releasedOn?.asExternalModel()
)
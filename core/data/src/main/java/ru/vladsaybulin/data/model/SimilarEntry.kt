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

import ru.vladsaybulin.model.SimilarEntry
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.network.models.anime.NetworkAnime
import ru.vladsaybulin.network.models.manga.NetworkManga

fun NetworkAnime.asSimilarEntry() = SimilarEntry(
    entryId = id,
    entryType = EntryType.Anime,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asExternalModel()
)

fun NetworkManga.asSimilarEntry() = SimilarEntry(
    entryId = id,
    entryType = EntryType.Manga,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asExternalModel()
)
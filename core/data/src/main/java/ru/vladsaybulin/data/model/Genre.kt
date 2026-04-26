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

import ru.vladsaybulin.database.models.filters.FilterGenreEntity
import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.network.models.common.NetworkGenre

fun NetworkGenre.asFilterEntity() = FilterGenreEntity(
    id = id,
    name = name,
    nameRu = russianName,
    entryType = entryType,
    kind = kind
)

fun NetworkGenre.asEntity() = GenreEntity(
    id = id,
    name = name,
    nameRu = russianName,
    entryType = entryType,
    kind = kind
)

fun NetworkGenre.asExternalModel() = Genre(
    id = id,
    englishName = name,
    russianName = russianName,
    entryType = entryType,
    kind = kind
)
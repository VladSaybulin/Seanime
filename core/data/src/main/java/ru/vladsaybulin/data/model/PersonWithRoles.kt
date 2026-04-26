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

import ru.vladsaybulin.database.models.anime.AnimePersonRolesEntity
import ru.vladsaybulin.database.models.manga.MangaPersonRolesEntity
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles

fun NetworkPersonWithRoles.asAnimePersonWithRoles(animeId: Long) = AnimePersonRolesEntity(
    personId = person.id,
    roles = roles,
    isMain = roles.isMainPersonRoles(),
    animeId = animeId
)

fun NetworkPersonWithRoles.asMangaPersonWithRoles(mangaId: Long) = MangaPersonRolesEntity(
    personId = person.id,
    roles = roles,
    isMain = roles.isMainPersonRoles(),
    mangaId = mangaId
)

fun List<String>.isMainPersonRoles() = any { it in MainRoles }

private val MainRoles = listOf(
    "Director",
    "Original Creator",
    "Story",
    "Art",
    "Story & Art"
)
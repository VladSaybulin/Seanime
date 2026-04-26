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

import ru.vladsaybulin.database.models.anime.AnimeCharacterEntity
import ru.vladsaybulin.database.models.anime.AnimePersonRolesEntity
import ru.vladsaybulin.database.models.character.CharacterEntity
import ru.vladsaybulin.database.models.manga.MangaCharacterEntity
import ru.vladsaybulin.database.models.manga.MangaPersonRolesEntity
import ru.vladsaybulin.database.models.person.PersonEntity
import ru.vladsaybulin.network.models.common.NetworkTitleRoles

fun NetworkTitleRoles.personEntityShells(): List<PersonEntity>? =
    authors?.takeIf { it.isNotEmpty() }
        ?.map { it.person.asEntity() }

fun NetworkTitleRoles.animePersonRolesEntities(animeId: Long): List<AnimePersonRolesEntity>? =
    authors?.takeIf { it.isNotEmpty() }
        ?.map {
            AnimePersonRolesEntity(
                animeId = animeId,
                personId = it.person.id,
                roles = it.roles,
                isMain = it.roles.isMainPersonRoles()
            )
        }

fun NetworkTitleRoles.mangaPersonRolesEntities(mangaId: Long): List<MangaPersonRolesEntity>? =
    authors?.takeIf { it.isNotEmpty() }
        ?.map {
            MangaPersonRolesEntity(
                mangaId = mangaId,
                personId = it.person.id,
                roles = it.roles,
                isMain = it.roles.isMainPersonRoles()
            )
        }

fun NetworkTitleRoles.characterEntityShells(): List<CharacterEntity>? =
    characters?.takeIf { it.isNotEmpty() }
        ?.map { it.character.asEntity() }

fun NetworkTitleRoles.animeCharacterEntities(animeId: Long): List<AnimeCharacterEntity>? =
    characters?.takeIf { it.isNotEmpty() }
        ?.map {
            AnimeCharacterEntity(
                animeId = animeId,
                characterId = it.character.id,
                isMain = it.isMain
            )
        }

fun NetworkTitleRoles.mangaCharacterEntities(mangaId: Long): List<MangaCharacterEntity>? =
    characters?.takeIf { it.isNotEmpty() }
        ?.map {
            MangaCharacterEntity(
                mangaId = mangaId,
                characterId = it.character.id,
                isMain = it.isMain
            )
        }
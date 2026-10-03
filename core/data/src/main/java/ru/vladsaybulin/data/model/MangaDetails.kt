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

import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.character.CharacterEntity
import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.database.models.genre.asExternalModel
import ru.vladsaybulin.database.models.manga.MangaCharacterReferenceWithRoleEntity
import ru.vladsaybulin.database.models.manga.MangaDetailsEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.MangaGenreCrossRef
import ru.vladsaybulin.database.models.manga.MangaPersonReferenceWithRolesEntity
import ru.vladsaybulin.database.models.manga.MangaPublisherCrossRef
import ru.vladsaybulin.database.models.manga.MangaRelatedEntity
import ru.vladsaybulin.database.models.manga.PopulatedMangaDetails
import ru.vladsaybulin.database.models.manga.PublisherEntity
import ru.vladsaybulin.database.models.manga.asExternalModel
import ru.vladsaybulin.database.models.person.PersonEntity
import ru.vladsaybulin.database.models.stats.asExternalModel
import ru.vladsaybulin.database.models.text.asExternalModel
import ru.vladsaybulin.database.models.title.PopulatedRelatedTitle
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.title.TitleDetails
import ru.vladsaybulin.network.models.common.NetworkTitleRoles
import ru.vladsaybulin.network.models.manga.NetworkMangaDetails

// ==========================================
// Network -> Database
// ==========================================

internal fun NetworkMangaDetails.asEntity() = MangaDetailsEntity(
    id = id,
    nameEn = nameEn,
    nameJp = nameJp,
    altNames = alternativeName ?: "",
    licenseName = licenseNameRu,
    description = descriptionHtml?.asSeanimeText()?.asSeanimeTextPOJO(),
    descriptionSource = descriptionSource,
    scoreStats = scoreStats.asDatabaseModel(),
    statusStats = userRateStatusStats.asDatabaseModel()
)

internal fun NetworkMangaDetails.extractGenreEntities(
    entities: MutableList<GenreEntity>,
    crossRefs: MutableList<MangaGenreCrossRef>
) {
    genres?.forEach { genre ->
        entities.add(genre.asEntity())
        crossRefs.add(MangaGenreCrossRef(mangaId = id, genreId = genre.id))
    }
}

internal fun NetworkMangaDetails.extractPublisherEntities(
    entities: MutableList<PublisherEntity>,
    crossRefs: MutableList<MangaPublisherCrossRef>
) {
    publishers.forEach { publisher ->
        entities.add(publisher.asEntity())
        crossRefs.add(MangaPublisherCrossRef(mangaId = id, publisherId = publisher.id))
    }
}

internal fun NetworkMangaDetails.extractRelatedEntities(
    mangaEntities: MutableList<MangaEntity>,
    animeEntities: MutableList<AnimeEntity>,
    relatedEntities: MutableList<MangaRelatedEntity>
) {
    val relatedTitles = related ?: return
    relatedTitles.forEachIndexed { index, item ->
        val mangaId = item.manga?.asEntity()
            ?.also { mangaEntities.add(it) }
            ?.id

        val animeId = item.anime?.asEntity()
            ?.also { animeEntities.add(it) }
            ?.id

        if (animeId != null || mangaId != null) {
            relatedEntities.add(
                MangaRelatedEntity(
                    mangaId = id,
                    relatedAnimeId = animeId,
                    relatedMangaId = mangaId,
                    relationType = item.relationType,
                    order = index
                )
            )
        }
    }
}

internal fun NetworkTitleRoles.extractMangaCharacters(
    mangaId: Long,
    characterEntities: MutableList<CharacterEntity>,
    rolesEntities: MutableList<MangaCharacterReferenceWithRoleEntity>
) {
    extractCharacters(
        roleEntities = rolesEntities,
        characterEntities = characterEntities
    ) { characterId, isMain ->
        MangaCharacterReferenceWithRoleEntity(
            mangaId = mangaId,
            characterId = characterId,
            isMainRole = isMain
        )
    }
}

internal fun NetworkTitleRoles.extractMangaPersons(
    mangaId: Long,
    personEntities: MutableList<PersonEntity>,
    rolesEntities: MutableList<MangaPersonReferenceWithRolesEntity>
) {
    extractPersons(
        roleEntities = rolesEntities,
        personEntities = personEntities
    ) { personId, roles ->
        MangaPersonReferenceWithRolesEntity(
            mangaId = mangaId,
            personId = personId,
            roles = roles
        )
    }
}

// ==========================================
// Database -> Domain
// ==========================================

internal fun PopulatedMangaDetails.asExternalModel(): TitleDetails = with(mangaDetailsEntity) {
    TitleDetails(
        id = id,
        type = EntryType.Manga,
        nameEn = nameEn,
        nameJp = nameJp,
        alternativeNames = altNames,
        licensedName = licenseName,
        description = description?.asExternalModel(),
        descriptionSource = descriptionSource,
        scoreStats = scoreStats.asExternalModel(),
        userRateStatusStats = statusStats.asExternalModel(),
        genres = genres.map { it.asExternalModel() },
        publishers = publishers.map { it.asExternalModel() },
    )
}


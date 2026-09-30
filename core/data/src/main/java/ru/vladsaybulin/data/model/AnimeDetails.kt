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

import ru.vladsaybulin.database.models.anime.AnimeCharacterReferenceWithRoleEntity
import ru.vladsaybulin.database.models.anime.AnimeDetailsEntity
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.AnimeGenreCrossRef
import ru.vladsaybulin.database.models.anime.AnimePersonReferenceWithRolesEntity
import ru.vladsaybulin.database.models.anime.AnimeRelatedEntity
import ru.vladsaybulin.database.models.anime.AnimeScreenshotEntity
import ru.vladsaybulin.database.models.anime.AnimeStudioCrossRef
import ru.vladsaybulin.database.models.anime.AnimeVideoEntity
import ru.vladsaybulin.database.models.anime.PopulatedAnimeDetails
import ru.vladsaybulin.database.models.anime.StudioEntity
import ru.vladsaybulin.database.models.character.CharacterEntity
import ru.vladsaybulin.database.models.common.SeasonPOJO
import ru.vladsaybulin.database.models.common.asExternalModel
import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.database.models.genre.asExternalModel
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.person.PersonEntity
import ru.vladsaybulin.database.models.stats.asExternalModel
import ru.vladsaybulin.database.models.text.asExternalModel
import ru.vladsaybulin.database.models.title.PopulatedRelatedTitle
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.search.TimePeriodAiring
import ru.vladsaybulin.model.title.TitleDetails
import ru.vladsaybulin.network.models.anime.NetworkAnimeDetails
import ru.vladsaybulin.network.models.common.NetworkTitleRoles
import kotlin.collections.emptyList

// ==========================================
// Network -> Database
// ==========================================

internal fun NetworkAnimeDetails.asEntity() = AnimeDetailsEntity(
    id = id,
    nameEn = nameEn,
    nameJp = nameJp,
    altNames = alternativeName ?: "",
    licenseNameRu = licenseNameRu,
    duration = duration ?: 0,
    nextEpisodeAt = nextEpisodeAt,
    season = season?.asDatabaseModel(),
    rating = AnimeRating.None, // Where rating?
    description = descriptionHtml?.asSeanimeText()?.asSeanimeTextPOJO(),
    descriptionSource = descriptionSource,
    subbers = subbers ?: emptyList(),
    dubbers = dubbers ?: emptyList(),
    scoreStats = scoreStats.asDatabaseModel(),
    statusStats = userRateStatusStats.asDatabaseModel(),
)

internal fun NetworkAnimeDetails.extractGenreEntities(
    entities: MutableList<GenreEntity>,
    crossRefs: MutableList<AnimeGenreCrossRef>
) {
    genres?.forEach { networkGenre ->
        entities.add(networkGenre.asEntity())
        crossRefs.add(AnimeGenreCrossRef(animeId = id, genreId = networkGenre.id))
    }
}

internal fun NetworkAnimeDetails.extractStudiosEntities(
    entities: MutableList<StudioEntity>,
    crossRefs: MutableList<AnimeStudioCrossRef>
) {
    studios.forEach { networkStudio ->
        entities.add(networkStudio.asEntity())
        crossRefs.add(AnimeStudioCrossRef(animeId = id, studioId = networkStudio.id))
    }
}

internal fun NetworkAnimeDetails.extractRelatedEntities(
    animeEntities: MutableList<AnimeEntity>,
    mangaEntities: MutableList<MangaEntity>,
    relatedEntities: MutableList<AnimeRelatedEntity>
) {
    val relatedTitles = related ?: return
    relatedTitles.forEachIndexed { index, item ->
        val animeId = item.anime?.asEntity()
            ?.also { animeEntities.add(it) }
            ?.id

        val mangaId = item.manga?.asEntity()
            ?.also { mangaEntities.add(it) }
            ?.id

        relatedEntities.add(
            AnimeRelatedEntity(
                animeId = id,
                relatedAnimeId = animeId,
                relatedMangaId = mangaId,
                relationType = item.relationType,
                order = index
            )
        )
    }
}

internal fun NetworkAnimeDetails.extractScreenshotEntities(
    entities: MutableList<AnimeScreenshotEntity>
) {
    screenshots.forEachIndexed { index, image ->
        entities.add(
            AnimeScreenshotEntity(
                animeId = id,
                order = index,
                previewUrl = image.previewUrl,
                originalUrl = image.originalUrl
            )
        )
    }
}

internal fun NetworkAnimeDetails.extractVideoEntities(
    entities: MutableList<AnimeVideoEntity>
) {
    videos?.forEachIndexed { index, video ->
        entities.add(
            AnimeVideoEntity(
                animeId = id,
                order = index,
                name = video.name,
                previewImageUrl = video.previewImageUrl,
                videoUrl = video.videoUrl,
                playerUrl = video.playerUrl,
                kind = video.kind
            )
        )
    }
}

internal fun NetworkTitleRoles.extractAnimeCharacters(
    animeId: Long,
    characterEntities: MutableList<CharacterEntity>,
    rolesEntities: MutableList<AnimeCharacterReferenceWithRoleEntity>
) {
    extractCharacters(
        roleEntities = rolesEntities,
        characterEntities = characterEntities
    ) { characterId, isMain ->
        AnimeCharacterReferenceWithRoleEntity(
            animeId = animeId,
            characterId = characterId,
            isMainRole = isMain
        )
    }
}

internal fun NetworkTitleRoles.extractAnimePersons(
    animeId: Long,
    personEntities: MutableList<PersonEntity>,
    rolesEntities: MutableList<AnimePersonReferenceWithRolesEntity>
) {
    extractPersons(
        roleEntities = rolesEntities,
        personEntities = personEntities
    ) { personId, roles ->
        AnimePersonReferenceWithRolesEntity(
            animeId = animeId,
            personId = personId,
            roles = roles
        )
    }
}

private fun TimePeriodAiring.Season.asDatabaseModel() = SeasonPOJO(
    seasonOfYear = seasonOfYear,
    year = year
)

// ==========================================
// Database -> Domain
// ==========================================

internal fun mergeAnimeDetailsToExternalModel(
    animeDetails: PopulatedAnimeDetails,
    relatedTitles: List<PopulatedRelatedTitle>,
): TitleDetails = with(animeDetails.animeDetailsEntity) {
    TitleDetails(
        id = id,
        type = EntryType.Anime,
        nameEn = nameEn,
        nameJp = nameJp,
        alternativeNames = altNames,
        licensedName = licenseNameRu,
        rating = rating,
        episodeDuration = duration,
        nextEpisodeAt = nextEpisodeAt,
        description = description?.asExternalModel(),
        descriptionSource = descriptionSource,
        subbers = subbers,
        dubbers = dubbers,
        scoreStats = scoreStats.asExternalModel(),
        userRateStatusStats = statusStats.asExternalModel(),
        season = season?.asExternalModel(),
        genres = animeDetails.genres.map { it.asExternalModel() },
        screenshots = animeDetails.screenshots.map { it.asExternalModel() },
        videos = animeDetails.videos.map { it.asExternalModel() },
        studios = animeDetails.studios.map { it.asExternalModel() },
        related = relatedTitles.map { it.asExternalModel() }
    )
}

internal fun AnimeScreenshotEntity.asExternalModel() = Image(
    previewUrl = previewUrl,
    originalUrl = originalUrl
)

internal fun AnimeVideoEntity.asExternalModel() = Video(
    name = name,
    previewImageUrl = previewImageUrl,
    videoUrl = videoUrl,
    playerUrl = playerUrl,
    kind = kind
)

internal fun StudioEntity.asExternalModel() = Studio(
    id = id,
    name = name,
    imageUrl = imageUrl
)
package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.anime.AnimeCharacterEntity
import ru.vladsaybulin.database.models.anime.AnimeDetailsEntity
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.AnimeGenreCrossRef
import ru.vladsaybulin.database.models.anime.AnimePersonRolesEntity
import ru.vladsaybulin.database.models.anime.AnimeRelatedEntity
import ru.vladsaybulin.database.models.anime.AnimeScreenshotEntity
import ru.vladsaybulin.database.models.anime.AnimeStudioCrossRef
import ru.vladsaybulin.database.models.anime.AnimeVideoEntity
import ru.vladsaybulin.model.anime.AnimeDetails
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.models.NetworkGenre
import ru.vladsaybulin.network.models.NetworkStudio
import ru.vladsaybulin.network.models.anime.NetworkAnimeDetails
import ru.vladsaybulin.network.models.anime.NetworkVideo
import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole
import ru.vladsaybulin.network.models.common.NetworkImage
import ru.vladsaybulin.network.models.common.NetworkStatisticsItem
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles
import ru.vladsaybulin.network.models.related.NetworkRelated

fun NetworkAnimeDetails.asExternalModel() = AnimeDetails(
    id = id,
    originalName = name,
    russianName = nameRu,
    englishName = nameRu,
    japaneseName = nameJp,
    alternativeName = alternativeName,
    licenseNameRu = licenseNameRu,
    poster = poster?.asExternalModel(),
    kind = kind,
    score = score ?: 0f,
    status = status,
    rating = rating,
    episodes = episodes,
    episodesAired = episodesAired,
    duration = duration,
    nextEpisodeAt = nextEpisodeAt,
    airedOn = airedOn?.asExternalModel(),
    releasedOn = releasedOn?.asExternalModel(),
    descriptionHtml = descriptionHtml,
    descriptionSource = descriptionSource,
    genres = genres?.map(NetworkGenre::asExternalModel),
    subbers = subbers,
    dubbers = dubbers,
    scoreStats = scoreStats?.map(NetworkStatisticsItem<Int>::asExternalModel),
    userRateStatusStats = userRateStatusStats?.map(NetworkStatisticsItem<UserRateStatus>::asExternalModel),
    studios = studios.map(NetworkStudio::asExternalModel),
    authors = authors?.map(NetworkPersonWithRoles::asExternalModel),
    characters = characters?.map(NetworkCharacterWithRole::asExternalModel),
    related = related?.map(NetworkRelated::asExternalModel),
    screenshots = screenshots.map(NetworkImage::asExternalModel),
    videos = videos?.map(NetworkVideo::asExternalModel)
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

fun NetworkAnimeDetails.asAnimeDetailsEntity() = AnimeDetailsEntity(
    id = id,
    nameEn = nameEn,
    nameJp = nameJp,
    altNames = alternativeName,
    licenseNameRu = licenseNameRu,
    rating = rating,
    duration = duration ?: 0,
    nextEpisodeAt = nextEpisodeAt,
    description = descriptionHtml ?: "",
    descriptionSource = descriptionSource,
    subbers = subbers,
    dubbers = dubbers,
    scoreStats = scoreStats?.map { it.asExternalModel() },
    statusStats = userRateStatusStats?.map { it.asExternalModel() },
)

fun NetworkAnimeDetails.personEntityShells() =
    authors?.map { it.person.asEntity() }

fun NetworkAnimeDetails.animeAuthorEntities() =
    authors?.map {
        AnimePersonRolesEntity(
            animeId = id,
            personId = it.person.id,
            rolesEn = it.roles,
            rolesRu = it.russianRoles
        )
    }

fun NetworkAnimeDetails.characterEntityShells() =
    characters?.map { it.character.asEntity() }

fun NetworkAnimeDetails.animeCharacterEntities() =
    characters?.map {
        AnimeCharacterEntity(
            animeId = id,
            characterId = it.character.id,
            isMain = it.isMain
        )
    }

fun NetworkAnimeDetails.genreEntityShells() =
    genres?.map { it.asEntity() }

fun NetworkAnimeDetails.genresCrossReferences() =
    genres?.map { AnimeGenreCrossRef(animeId = id, genreId = it.id) }

fun NetworkAnimeDetails.animeScreenshotEntities() =
    screenshots.mapIndexed { index, image ->
        AnimeScreenshotEntity(
            animeId = id,
            order = index,
            previewUrl = image.previewUrl,
            originalUrl = image.originalUrl
        )
    }

fun NetworkAnimeDetails.animeVideoEntities() =
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

fun NetworkAnimeDetails.relatedAnimeEntityShell() =
    related?.mapNotNull { it.anime?.asEntity() }

fun NetworkAnimeDetails.relatedMangaEntityShell() =
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
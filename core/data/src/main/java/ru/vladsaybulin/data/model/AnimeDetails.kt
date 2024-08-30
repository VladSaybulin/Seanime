package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.anime.AnimeDetailsEntity
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.AnimeGenreCrossRef
import ru.vladsaybulin.database.models.anime.AnimeRelatedEntity
import ru.vladsaybulin.database.models.anime.AnimeScreenshotEntity
import ru.vladsaybulin.database.models.anime.AnimeStudioCrossRef
import ru.vladsaybulin.database.models.anime.AnimeVideoEntity
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
    scoreStats = scoreStats?.map { it.asExternalModel() },
    statusStats = userRateStatusStats?.map { it.asExternalModel() },
    season = null //TODO season
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
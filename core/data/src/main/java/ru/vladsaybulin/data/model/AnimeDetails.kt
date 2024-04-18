package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.anime.AnimeEntity
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

fun NetworkAnimeDetails.asEntity() = AnimeEntity(
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
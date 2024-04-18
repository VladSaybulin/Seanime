package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.model.manga.MangaDetails
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.models.NetworkGenre
import ru.vladsaybulin.network.models.NetworkPublisher
import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole
import ru.vladsaybulin.network.models.common.NetworkStatisticsItem
import ru.vladsaybulin.network.models.manga.NetworkMangaDetails
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles
import ru.vladsaybulin.network.models.related.NetworkRelated

fun NetworkMangaDetails.asExternalModel() = MangaDetails(
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
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asExternalModel(),
    releasedOn = releasedOn?.asExternalModel(),
    descriptionHtml = descriptionHtml,
    descriptionSource = descriptionSource,
    genres = genres?.map(NetworkGenre::asExternalModel),
    scoreStats = scoreStats?.map(NetworkStatisticsItem<Int>::asExternalModel),
    userRateStatusStats = userRateStatusStats?.map(NetworkStatisticsItem<UserRateStatus>::asExternalModel),
    publishers = publishers.map(NetworkPublisher::asExternalModel),
    authors = authors?.map(NetworkPersonWithRoles::asExternalModel),
    characters = characters?.map(NetworkCharacterWithRole::asExternalModel),
    related = related?.map(NetworkRelated::asExternalModel)
)

fun NetworkMangaDetails.asEntity() = MangaEntity(
    id = id,
    originalName = name,
    russianName = nameRu,
    poster = poster?.asPOJO(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asPOJO(),
    releasedOn = releasedOn?.asPOJO()
)
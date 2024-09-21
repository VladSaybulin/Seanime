package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.userrate.UserRateEntity
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.network.models.NetworkAnime

fun NetworkAnime.asEntity() = AnimeEntity(
    id = id,
    originalName = originalName,
    russianName = russianName?.ifEmpty { null },
    poster = poster?.asPOJO(),
    kind = kind,
    status = status,
    score = score,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.asPOJO(),
    releasedOn = releasedOn?.asPOJO()
)

fun NetworkAnime.userRateEntityShell() = userRate?.let { userRate ->
    UserRateEntity(
        id = userRate.id,
        animeId = id,
        mangaId = null,
        status = userRate.status,
        score = userRate.score,
        episodes = userRate.episodes,
        chapters = 0,
        volumes = 0,
        rewatches = userRate.rewatches,
        text = userRate.text ?: "",
        createdAt = userRate.createdAt,
        updatedAt = userRate.updatedAt
    )
}

fun NetworkAnime.asExternalModel() = Anime(
    id = id,
    name = originalName,
    russianName = russianName,
    poster = poster?.asExternalModel(),
    kind = kind,
    status = status,
    score = score,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.asExternalModel(),
    releasedOn = releasedOn?.asExternalModel(),
    userRate = userRate?.asExternalModel()
)

fun Anime.asPOJO() = AnimeEntity(
    id = id,
    originalName = name,
    russianName = russianName,
    poster = poster?.asPOJO(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.asPOJO(),
    releasedOn = releasedOn?.asPOJO()
)
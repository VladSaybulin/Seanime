package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.userrate.UserRateEntity
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.network.models.NetworkAnime

fun NetworkAnime.asEntity() = AnimeEntity(
    id = id,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asEntity(),
    kind = kind,
    status = status,
    score = score,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.asEntity(),
    releasedOn = releasedOn?.asEntity()
)

fun NetworkAnime.userRateEntityShell() = userRate?.let { userRate ->
    UserRateEntity(
        id = userRate.id,
        animeId = id,
        mangaId = null,
        status = userRate.status,
        score = userRate.score,
        episodes = episodes,
        chapters = 0,
        volumes = 0,
        rewatches = userRate.rewatches,
        text = userRate.text ?: "",
        createdAt = userRate.createdAt,
        updatedAt = userRate.updatedAt
    )
}

fun NetworkAnime.asAnime() = Anime(
    id = id,
    name = originalName,
    russianName = russianName,
    poster = poster?.asPoster(),
    kind = kind,
    status = status,
    score = score,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.asIncompleteDate(),
    releasedOn = releasedOn?.asIncompleteDate(),
    userRate = userRate?.asExternalModel()
)

fun Anime.asEntity() = AnimeEntity(
    id = id,
    originalName = name,
    russianName = russianName,
    poster = poster?.asEntity(),
    kind = kind,
    status = status,
    score = score ?: 0f,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.asEntity(),
    releasedOn = releasedOn?.asEntity()
)
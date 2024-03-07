package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.AnimeDbo
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.network.models.AnimeDto

fun AnimeDto.asDbo() = AnimeDbo(
    id = id,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asDbo(),
    kind = kind,
    status = status,
    score = score,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.asIncompleteDateDbo(),
    releasedOn = releasedOn?.asIncompleteDateDbo()
)

fun AnimeDto.asAnime() = Anime(
    id = id,
    originalName = originalName,
    russianName = russianName,
    poster = poster?.asPoster(),
    kind = kind,
    status = status,
    score = score,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.asIncompleteDate(),
    releasedOn = releasedOn?.asIncompleteDate()
)
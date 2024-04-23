package ru.vladsaybulin.core.domain

import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeDetails
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaDetails

private fun AnimeDetails.asBrief() = Anime(
    id = id,
    name = originalName,
    russianName = russianName,
    poster = poster,
    kind = kind,
    status = status,
    score = score,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn,
    releasedOn = releasedOn,
    userRate = null
)

private fun MangaDetails.asBrief() = Manga(
    id = id,
    name = originalName,
    russianName = russianName,
    poster = poster,
    kind = kind,
    status = status,
    score = score,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn,
    releasedOn = releasedOn
)
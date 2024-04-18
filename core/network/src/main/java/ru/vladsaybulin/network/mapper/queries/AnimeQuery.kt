package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.AnimeQuery
import ru.vladsaybulin.network.mapper.enums.asAnimeKind
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.models.NetworkAnime
import ru.vladsaybulin.network.models.IncompleteDateDto
import ru.vladsaybulin.network.models.PosterDto
import ru.vladsaybulin.network.models.UserRateDto

internal fun AnimeQuery.Anime.asDto() = NetworkAnime(
    id = id,
    originalName = name,
    russianName = russian,
    poster = poster?.asDto(),
    kind = kind.asAnimeKind(),
    status = status.asEntryStatus(),
    score = score?.toFloat() ?: 0f,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.asDto(),
    releasedOn = releasedOn?.asDto(),
    userRate = userRate?.asDto()
)

private fun AnimeQuery.Poster.asDto() = PosterDto(originalUrl, previewUrl)

private fun AnimeQuery.AiredOn.asDto() = IncompleteDateDto(day, month, year)

private fun AnimeQuery.ReleasedOn.asDto() = IncompleteDateDto(day, month, year)

private fun AnimeQuery.UserRate.asDto() = UserRateDto(
    id = id,
    createdAt = createdAt,
    updatedAt = updatedAt,
    status = status.asUserRateStatus(),
    score = score,
    episodes = episodes,
    chapters = chapters,
    volumes = volumes,
    rewatches = rewatches,
    text = text ?: ""
)
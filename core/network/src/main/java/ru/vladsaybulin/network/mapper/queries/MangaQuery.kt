package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.MangaQuery
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.mapper.enums.asMangaKind
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.models.IncompleteDateDto
import ru.vladsaybulin.network.models.NetworkManga
import ru.vladsaybulin.network.models.PosterDto
import ru.vladsaybulin.network.models.UserRateDto

internal fun MangaQuery.Manga.asNetworkModel() = NetworkManga(
    id = id,
    originalName = name,
    russianName = russian,
    poster = poster?.asNetworkModel(),
    kind = kind.asMangaKind(),
    status = status.asEntryStatus(),
    score = score?.toFloat() ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asNetworkModel(),
    releasedOn = releasedOn?.asNetworkModel(),
    userRate = userRate?.asNetworkModel()
)

private fun MangaQuery.Poster.asNetworkModel() = PosterDto(originalUrl, previewUrl)

private fun MangaQuery.AiredOn.asNetworkModel() = IncompleteDateDto(day, month, year)

private fun MangaQuery.ReleasedOn.asNetworkModel() = IncompleteDateDto(day, month, year)

private fun MangaQuery.UserRate.asNetworkModel() = UserRateDto(
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
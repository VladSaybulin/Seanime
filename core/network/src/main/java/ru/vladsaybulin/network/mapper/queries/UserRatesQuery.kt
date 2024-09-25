package ru.vladsaybulin.network.mapper.queries

import kotlinx.datetime.LocalDate
import ru.vladsaybulin.core.network.graphql.UserRatesQuery
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.network.mapper.enums.asAnimeKind
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.mapper.enums.asMangaKind
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.models.NetworkAnime
import ru.vladsaybulin.network.models.NetworkIncompleteDate
import ru.vladsaybulin.network.models.NetworkManga
import ru.vladsaybulin.network.models.NetworkUserRate
import ru.vladsaybulin.network.models.UserRateWithEntryDto
import ru.vladsaybulin.network.models.common.NetworkImage

internal fun UserRatesQuery.UserRate.asNetworkModel() = UserRateWithEntryDto(
    networkUserRate = NetworkUserRate(
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
    ),
    networkAnime = anime?.asNetworkModel(),
    networkManga = manga?.asNetworkModel()
)

private fun UserRatesQuery.Anime.asNetworkModel() = NetworkAnime(
    id = id,
    originalName = name,
    russianName = russian,
    poster = poster?.asNetworkModel(),
    kind = kind.asAnimeKind(),
    score = score?.toFloat() ?: 0f,
    status = status.asEntryStatus(),
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.date?.asIncompleteDate(),
    releasedOn = releasedOn?.date?.asIncompleteDate(),
)

private fun UserRatesQuery.Manga.asNetworkModel() = NetworkManga(
    id = id,
    originalName = name,
    russianName = russian,
    poster = poster?.asNetworkModel(),
    kind = kind.asMangaKind(),
    score = score?.toFloat() ?: 0f,
    status = status.asEntryStatus(),
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.date?.asIncompleteDate(),
    releasedOn = releasedOn?.date?.asIncompleteDate(),
)

private fun UserRatesQuery.Poster1.asNetworkModel() = NetworkImage(
    originalUrl = originalUrl,
    previewUrl = main2xUrl
)

private fun UserRatesQuery.Poster.asNetworkModel() = NetworkImage(
    originalUrl = originalUrl,
    previewUrl = main2xUrl
)

private fun LocalDate.asIncompleteDate(): NetworkIncompleteDate {
    val year = year
    val month = dayOfMonth.takeIf { it != 1 || dayOfMonth != 1 }
    val day = dayOfMonth.takeIf { month != null && it != 1 }
    return NetworkIncompleteDate(day, month, year)
}
package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.MangaUserRatesQuery
import ru.vladsaybulin.network.mapper.enums.asEntryStatus
import ru.vladsaybulin.network.mapper.enums.asMangaKind
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.models.NetworkIncompleteDate
import ru.vladsaybulin.network.models.NetworkManga
import ru.vladsaybulin.network.models.NetworkUserRate
import ru.vladsaybulin.network.models.UserRateWithEntryDto
import ru.vladsaybulin.network.models.common.NetworkImage

internal fun MangaUserRatesQuery.UserRate.asNetworkModels(): UserRateWithEntryDto {
    require(manga != null)
    return UserRateWithEntryDto(
        networkUserRate = NetworkUserRate(
            id = id,
            createdAt = createdAt,
            updatedAt = updatedAt,
            status = status.asUserRateStatus(),
            score = score,
            episodes = 0,
            chapters = chapters,
            volumes = volumes,
            rewatches = rewatches,
            text = text ?: ""
        ),
        networkManga = manga!!.asNetworkModel(),
        networkAnime = null
    )
}

private fun MangaUserRatesQuery.Manga.asNetworkModel() = NetworkManga(
    id = id,
    originalName = name,
    russianName = russian,
    poster = poster?.run { NetworkImage(originalUrl, main2xUrl) },
    kind = kind.asMangaKind(),
    status = status.asEntryStatus(),
    score = score?.toFloat() ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.run { NetworkIncompleteDate(day, month, year) },
    releasedOn = releasedOn?.run { NetworkIncompleteDate(day, month, year) },
)
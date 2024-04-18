package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.MangaUserRateQuery
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.models.NetworkUserRate

fun MangaUserRateQuery.UserRate.asNetworkModel() = NetworkUserRate(
    id = id,
    status = status.asUserRateStatus(),
    score = score,
    episodes = 0,
    chapters = chapters,
    volumes = volumes,
    rewatches = rewatches,
    text = text ?: "",
    createdAt = createdAt,
    updatedAt = updatedAt
)

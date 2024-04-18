package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.AnimeUserRateQuery
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.models.NetworkUserRate

fun AnimeUserRateQuery.UserRate.asNetworkModel() = NetworkUserRate(
    id = id,
    status = status.asUserRateStatus(),
    score = score,
    episodes = episodes,
    chapters = 0,
    volumes = 0,
    rewatches = rewatches,
    text = text ?: "",
    createdAt = createdAt,
    updatedAt = updatedAt
)

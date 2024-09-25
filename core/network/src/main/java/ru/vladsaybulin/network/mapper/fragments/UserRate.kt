package ru.vladsaybulin.network.mapper.fragments

import ru.vladsaybulin.core.network.graphql.fragment.UserRateFragment
import ru.vladsaybulin.network.mapper.enums.asUserRateStatus
import ru.vladsaybulin.network.models.userrate.NetworkUserRate

internal fun UserRateFragment.asNetworkModel() = NetworkUserRate(
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
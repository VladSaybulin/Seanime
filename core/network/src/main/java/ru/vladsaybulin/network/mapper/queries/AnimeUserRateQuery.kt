package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.AnimeUserRateQuery
import ru.vladsaybulin.network.mapper.fragments.asNetworkModel

internal fun AnimeUserRateQuery.UserRate.asNetworkModel() = userRateFragment.asNetworkModel()

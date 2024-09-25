package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.MangaUserRateQuery
import ru.vladsaybulin.network.mapper.fragments.asNetworkModel

internal fun MangaUserRateQuery.UserRate.asNetworkModel() = userRateFragment.asNetworkModel()

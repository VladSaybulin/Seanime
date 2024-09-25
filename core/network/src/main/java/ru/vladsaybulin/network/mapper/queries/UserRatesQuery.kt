package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.UserRatesQuery
import ru.vladsaybulin.network.mapper.fragments.asNetworkModel
import ru.vladsaybulin.network.models.userrate.NetworkUserRateWithTitle

internal fun UserRatesQuery.UserRate.asNetworkModel() = NetworkUserRateWithTitle(
    networkUserRate = userRateFragment.asNetworkModel(),
    networkAnime = anime?.animeWithLocalDateFragment?.asNetworkModel(),
    networkManga = manga?.mangaWithLocalDateFragment?.asNetworkModel()
)
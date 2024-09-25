package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.MangaUserRatesQuery
import ru.vladsaybulin.network.mapper.fragments.asNetworkModel
import ru.vladsaybulin.network.models.userrate.NetworkUserRateWithTitle

internal fun MangaUserRatesQuery.UserRate.asNetworkModels(): NetworkUserRateWithTitle {
    return NetworkUserRateWithTitle(
        networkUserRate = userRateFragment.asNetworkModel(),
        networkManga = checkNotNull(manga).mangaFragment.asNetworkModel(),
        networkAnime = null
    )
}
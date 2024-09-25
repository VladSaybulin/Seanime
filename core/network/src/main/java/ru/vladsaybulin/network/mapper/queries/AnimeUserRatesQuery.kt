package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.AnimeUserRatesQuery
import ru.vladsaybulin.network.mapper.fragments.asNetworkModel
import ru.vladsaybulin.network.models.userrate.NetworkUserRateWithTitle

internal fun AnimeUserRatesQuery.UserRate.asNetworkModel(): NetworkUserRateWithTitle {
    return NetworkUserRateWithTitle(
        networkUserRate = userRateFragment.asNetworkModel(),
        networkAnime = checkNotNull(anime).animeFragment.asNetworkModel(),
        networkManga = null
    )
}
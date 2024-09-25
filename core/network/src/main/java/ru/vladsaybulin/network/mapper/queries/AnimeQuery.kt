package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.AnimeQuery
import ru.vladsaybulin.network.mapper.fragments.asNetworkModel

internal fun AnimeQuery.Anime.asNetworkModels() =
    animeFragment.asNetworkModel(userRate?.userRateFragment?.asNetworkModel())
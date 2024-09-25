package ru.vladsaybulin.network.mapper.queries

import ru.vladsaybulin.core.network.graphql.MangaQuery
import ru.vladsaybulin.network.mapper.fragments.asNetworkModel

internal fun MangaQuery.Manga.asNetworkModel() =
    mangaFragment.asNetworkModel(userRate?.userRateFragment?.asNetworkModel())
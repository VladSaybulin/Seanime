package ru.vladsaybulin.network.mapper.fragments

import ru.vladsaybulin.core.network.graphql.fragment.PosterFragment
import ru.vladsaybulin.network.models.common.NetworkImage

internal fun PosterFragment.asNetworkModel() =
    NetworkImage(originalUrl = originalUrl, previewUrl = main2xUrl)
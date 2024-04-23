package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.auth.BuildConfig
import ru.vladsaybulin.database.models.anime.StudioEntity
import ru.vladsaybulin.database.models.filters.FilterStudioEntity
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.network.models.NetworkStudio

fun NetworkStudio.asFilterEntity() = FilterStudioEntity(
    id = id,
    name = name,
    imageUrl = "${BuildConfig.BASE_URL}$image"
)

fun NetworkStudio.asEntity() = StudioEntity(
    id = id,
    name = name,
    imageUrl = "${BuildConfig.BASE_URL}$image"
)

fun NetworkStudio.asExternalModel() = Studio(
    id = id,
    name = name,
    imageUrl = "${BuildConfig.BASE_URL}$image"
)
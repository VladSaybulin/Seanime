package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.auth.BuildConfig
import ru.vladsaybulin.database.models.anime.StudioEntity
import ru.vladsaybulin.network.models.NetworkStudio

fun NetworkStudio.asEntity() = StudioEntity(
    id = id,
    name = name,
    imageUrl = "${BuildConfig.BASE_URL}$image"
)
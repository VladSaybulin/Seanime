package ru.vladsaybulin.network.models

import kotlinx.serialization.Serializable
import ru.vladsaybulin.network.util.serializers.PosterSerializer

@Serializable(with = PosterSerializer::class)
data class PosterDto(
    val originalUrl: String,
    val previewUrl: String
)
package ru.vladsaybulin.network.models.common

import kotlinx.serialization.Serializable
import ru.vladsaybulin.network.util.serializers.ImageSerializer

@Serializable(ImageSerializer::class)
class NetworkImage(
    val originalUrl: String,
    val previewUrl: String
)
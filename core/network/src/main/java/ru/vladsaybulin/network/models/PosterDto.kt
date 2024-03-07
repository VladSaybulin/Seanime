package ru.vladsaybulin.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PosterDto(
    @SerialName("original") val originalUrl: String,
    @SerialName("preview") val previewUrl: String
)
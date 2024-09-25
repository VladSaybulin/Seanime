package ru.vladsaybulin.network.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserImageDto(
    @SerialName("x160") val x160Url: String,
    @SerialName("x148") val x148Url: String,
    @SerialName("x80") val x80Url: String,
    @SerialName("x64") val x64Url: String,
    @SerialName("x48") val x48Url: String,
    @SerialName("x32") val x32Url: String,
    @SerialName("x16") val x16Url: String
)

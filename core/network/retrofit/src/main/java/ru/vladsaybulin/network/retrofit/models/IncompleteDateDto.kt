package ru.vladsaybulin.network.retrofit.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IncompleteDateDto(
    @SerialName("day") val day: Int?,
    @SerialName("month") val month: Int?,
    @SerialName("year") val year: Int?
)
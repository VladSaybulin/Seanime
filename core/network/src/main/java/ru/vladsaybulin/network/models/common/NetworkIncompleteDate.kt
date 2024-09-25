package ru.vladsaybulin.network.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkIncompleteDate(
    @SerialName("day") val day: Int?,
    @SerialName("month") val month: Int?,
    @SerialName("year") val year: Int?
)
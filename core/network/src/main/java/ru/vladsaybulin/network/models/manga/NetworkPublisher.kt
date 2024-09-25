package ru.vladsaybulin.network.models.manga

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPublisher(
    @SerialName("id")
    val id: Long,

    @SerialName("name")
    val name: String,
)
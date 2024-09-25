package ru.vladsaybulin.network.models.forum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkForum(
    @SerialName("id") val id: Long,
    @SerialName("position") val position: Int,
    @SerialName("name") val name: String,
    @SerialName("permalink") val permalink: String,
    @SerialName("url") val url: String
)
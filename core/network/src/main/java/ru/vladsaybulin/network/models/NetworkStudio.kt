package ru.vladsaybulin.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkStudio(

    @SerialName("id")
    val id: Long,

    @SerialName("name")
    val name: String,

    @SerialName("image")
    val image: String?
)
package ru.vladsaybulin.network.models.person

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vladsaybulin.network.models.common.NetworkImage

@Serializable
data class NetworkPerson(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("russian") val nameRu: String?,
    @SerialName("image") val image: NetworkImage?
)
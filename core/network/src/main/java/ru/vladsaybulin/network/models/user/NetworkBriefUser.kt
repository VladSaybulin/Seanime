package ru.vladsaybulin.network.models.user

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkBriefUser(
    @SerialName("id") val id: Long,
    @SerialName("nickname") val nickname: String,
    @SerialName("avatar") val avatarUrl: String,
    @SerialName("image") val image: UserImageDto,
    @SerialName("last_online_at") val lastOnlineAt: Instant,
    @SerialName("url") val url: String
)
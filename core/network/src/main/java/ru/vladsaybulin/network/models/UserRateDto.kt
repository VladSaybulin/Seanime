package ru.vladsaybulin.network.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.util.serializers.UserRateStatusSerializer

@Serializable
data class UserRateDto(
    @SerialName("id") val id: Long,
    @SerialName("create_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
    @Serializable(UserRateStatusSerializer::class)
    @SerialName("status")
    val status: UserRateStatus,
    @SerialName("score") val score: Int,
    @SerialName("episodes") val episodes: Int,
    @SerialName("chapters") val chapters: Int,
    @SerialName("volumes") val volumes: Int,
    @SerialName("rewatches") val rewatches: Int,
    @SerialName("text") val text: String?
)
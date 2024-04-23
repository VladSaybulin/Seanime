package ru.vladsaybulin.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.util.serializers.UserRateStatusSerializer
import ru.vladsaybulin.network.util.serializers.UserRateTargetTypeSerializer

@Serializable
class CreateUserRateDto(
    @SerialName("user_id") val userId: Long,
    @SerialName("target_type")
    @Serializable(UserRateTargetTypeSerializer::class)
    val targetType: EntryType,
    @SerialName("target_id") val targetId: Long,
    @SerialName("status")
    @Serializable(UserRateStatusSerializer::class)
    val status: UserRateStatus,
    @SerialName("score") val score: Int?,
    @SerialName("episodes") val episodes: Int?,
    @SerialName("chapters") val chapters: Int?,
    @SerialName("volumes") val volumes: Int?,
    @SerialName("rewatches") val rewatches: Int?,
    @SerialName("text") val text: String?
)
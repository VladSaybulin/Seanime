package ru.vladsaybulin.network.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.util.serializers.UserRateStatusSerializer

@Serializable
class UserRateWithEntryLinkDto(
    @SerialName("id") val id: Long,
    @SerialName("target_id") val entryId: Long,
    @SerialName("target_type") val entryType: EntryType,
    @SerialName("score") val score: Int,
    @Serializable(UserRateStatusSerializer::class)
    @SerialName("status")
    val status: UserRateStatus,
    @SerialName("episodes") val episodes: Int,
    @SerialName("chapters") val chapters: Int,
    @SerialName("volumes") val volumes: Int,
    @SerialName("rewatches") val rewatches: Int,
    @SerialName("text") val text: String?,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("updated_at") val updatedAt: Instant,
)
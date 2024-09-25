package ru.vladsaybulin.network.models.userrate

import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.util.serializers.UserRateStatusSerializer

@Serializable
class UpdateUserRateRequest(
    @Serializable(UserRateStatusSerializer::class)
    val status: UserRateStatus,
    val score: Int?,
    val episodes: Int?,
    val chapters: Int?,
    val volumes: Int?,
    val rewatches: Int?,
    val text: String?
)
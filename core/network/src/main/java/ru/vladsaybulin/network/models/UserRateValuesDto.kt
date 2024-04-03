package ru.vladsaybulin.network.models

import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.UserRateStatus

@Serializable
class UserRateValuesDto(
    val status: UserRateStatus,
    val score: Int,
    val episodes: Int,
    val chapters: Int,
    val volumes: Int,
    val rewatches: Int,
    val text: String
)
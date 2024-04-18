package ru.vladsaybulin.model.userrate

import kotlinx.datetime.Instant

class UserRate(
    val id: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
    val status: UserRateStatus,
    val score: Int,
    val episodes: Int,
    val chapters: Int,
    val volumes: Int,
    val rewatches: Int,
    val text: String
)
package ru.vladsaybulin.model

class UserRateValues(
    val status: UserRateStatus,
    val score: Int,
    val episodes: Int?,
    val chapters: Int?,
    val volumes: Int?,
    val rewatches: Int,
    val text: String
)
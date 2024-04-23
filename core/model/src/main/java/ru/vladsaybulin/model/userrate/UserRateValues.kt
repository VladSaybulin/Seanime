package ru.vladsaybulin.model.userrate

class UserRateValues(
    val status: UserRateStatus,
    val score: Int? = null,
    val episodes: Int? = null,
    val chapters: Int? = null,
    val volumes: Int? = null,
    val rewatches: Int? = null,
    val text: String? = null
)
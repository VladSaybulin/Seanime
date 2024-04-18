package ru.vladsaybulin.model

data class UserRateWithEntry(
    val anime: Anime? = null,
    val manga: Manga? = null,
    val userRate: UserRate
)
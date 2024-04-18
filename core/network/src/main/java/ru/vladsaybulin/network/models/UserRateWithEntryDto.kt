package ru.vladsaybulin.network.models

data class UserRateWithEntryDto(
    val networkUserRate: NetworkUserRate,
    val networkAnime: NetworkAnime?,
    val networkManga: NetworkManga?
)
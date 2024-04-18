package ru.vladsaybulin.network.models

data class UserRateWithEntryDto(
    val userRateDto: UserRateDto,
    val networkAnime: NetworkAnime?,
    val networkManga: NetworkManga?
)
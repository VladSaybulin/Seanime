package ru.vladsaybulin.network.models.userrate

import ru.vladsaybulin.network.models.anime.NetworkAnime
import ru.vladsaybulin.network.models.manga.NetworkManga

data class NetworkUserRateWithTitle(
    val networkUserRate: NetworkUserRate,
    val networkAnime: NetworkAnime?,
    val networkManga: NetworkManga?
)
package ru.vladsaybulin.network.models.related

import ru.vladsaybulin.model.related.RelationType
import ru.vladsaybulin.network.models.anime.NetworkAnime
import ru.vladsaybulin.network.models.manga.NetworkManga

data class NetworkRelated(
    val relationType: RelationType,
    val anime: NetworkAnime?,
    val manga: NetworkManga?
)
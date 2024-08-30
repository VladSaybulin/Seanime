package ru.vladsaybulin.network.models.manga

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.models.NetworkGenre
import ru.vladsaybulin.network.models.NetworkIncompleteDate
import ru.vladsaybulin.network.models.NetworkPublisher
import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole
import ru.vladsaybulin.network.models.common.NetworkImage
import ru.vladsaybulin.network.models.common.NetworkStatisticsItem
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles
import ru.vladsaybulin.network.models.related.NetworkRelated

data class NetworkMangaDetails(
    val id: Long,
    val name: String,
    val nameRu: String?,
    val nameEn: String?,
    val nameJp: String?,
    val alternativeName: List<String>,
    val licenseNameRu: String?,
    val poster: NetworkImage?,
    val kind: MangaKind,
    val score: Float?,
    val status: EntryStatus,
    val chapters: Int,
    val volumes: Int,
    val airedOn: NetworkIncompleteDate?,
    val releasedOn: NetworkIncompleteDate?,
    val descriptionHtml: String?,
    val descriptionSource: String?,
    val genres: List<NetworkGenre>?,
    val scoreStats: List<NetworkStatisticsItem<Int>>?,
    val userRateStatusStats: List<NetworkStatisticsItem<UserRateStatus>>?,
    val publishers: List<NetworkPublisher>,
    val related: List<NetworkRelated>?
)
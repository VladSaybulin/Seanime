package ru.vladsaybulin.network.models.anime

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.search.TimePeriodAiring
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.models.common.NetworkGenre
import ru.vladsaybulin.network.models.common.NetworkIncompleteDate
import ru.vladsaybulin.network.models.common.NetworkImage
import ru.vladsaybulin.network.models.common.NetworkStatisticsItem
import ru.vladsaybulin.network.models.related.NetworkRelated

data class NetworkAnimeDetails(
    val id: Long,
    val name: String,
    val nameRu: String?,
    val nameEn: String?,
    val nameJp: String?,
    val alternativeName: List<String>,
    val licenseNameRu: String?,
    val poster: NetworkImage?,
    val kind: AnimeKind,
    val score: Float?,
    val status: EntryStatus,
    val rating: AnimeRating,
    val episodes: Int,
    val episodesAired: Int,
    val duration: Int?,
    val nextEpisodeAt: Instant?,
    val airedOn: NetworkIncompleteDate?,
    val releasedOn: NetworkIncompleteDate?,
    val season: TimePeriodAiring.Season?,
    val descriptionHtml: String?,
    val descriptionSource: String?,
    val genres: List<NetworkGenre>?,
    val subbers: List<String>?,
    val dubbers: List<String>?,
    val scoreStats: List<NetworkStatisticsItem<Int>>?,
    val userRateStatusStats: List<NetworkStatisticsItem<UserRateStatus>>?,
    val studios: List<NetworkStudio>,
    val related: List<NetworkRelated>?,
    val screenshots: List<NetworkImage>,
    val videos: List<NetworkVideo>?
)
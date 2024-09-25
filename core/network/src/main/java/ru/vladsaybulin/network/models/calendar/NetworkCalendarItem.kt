package ru.vladsaybulin.network.models.calendar

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vladsaybulin.network.models.anime.NetworkAnime

@Serializable
data class NetworkCalendarItem(
    @SerialName("next_episode") val nextEpisode: Int,
    @SerialName("next_episode_at") val nextEpisodeAt: Instant,
    @SerialName("duration") val duration: Int?,
    @SerialName("anime") val anime: NetworkAnime,
)
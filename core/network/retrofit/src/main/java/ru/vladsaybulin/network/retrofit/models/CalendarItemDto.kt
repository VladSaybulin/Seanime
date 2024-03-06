package ru.vladsaybulin.network.retrofit.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CalendarItemDto(
    @SerialName("next_episode") val nextEpisode: Int,
    @SerialName("next_episode_at") val nextEpisodeAt: Instant,
    @SerialName("duration") val duration: Int?,
    @SerialName("anime") val anime: AnimeDto,
)
package ru.vladsaybulin.network.retrofit.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CalendarDto(
    val nextEpisode: Int,
    val nextEpisodeAt: Instant,
    val duration: Int,
    val anime: AnimeDto,
)
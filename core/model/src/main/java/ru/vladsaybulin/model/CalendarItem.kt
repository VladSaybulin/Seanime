package ru.vladsaybulin.model

import kotlinx.datetime.Instant

data class CalendarItem(
    val anime: Anime,
    val nextEpisode: Int,
    val nextEpisodeAt: Instant,
    val duration: Int?
)
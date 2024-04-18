package ru.vladsaybulin.model.calendar

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.previewAnimes
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

data class CalendarItem(
    val anime: Anime,
    val nextEpisode: Int,
    val nextEpisodeAt: Instant,
    val duration: Duration?
)

val previewCalendarItems = listOf(
    CalendarItem(
        anime = previewAnimes[0],
        nextEpisode = 2,
        nextEpisodeAt = Clock.System.now(),
        duration = 30.minutes
    ),
    CalendarItem(
        anime = previewAnimes[1],
        nextEpisode = 0,
        nextEpisodeAt = Clock.System.now(),
        duration = null
    ),
)
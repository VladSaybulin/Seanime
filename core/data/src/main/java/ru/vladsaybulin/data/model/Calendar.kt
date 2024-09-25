package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.calendar.CalendarItemEntity
import ru.vladsaybulin.network.models.calendar.NetworkCalendarItem

fun NetworkCalendarItem.asEntity() = CalendarItemEntity(
    nextEpisode = nextEpisode,
    nextEpisodeAt = nextEpisodeAt,
    duration = duration.takeIf { it != 0 },
    animeId = anime.id
)

fun NetworkCalendarItem.animeShell() = anime.asEntity()
package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.calendar.CalendarItemDbo
import ru.vladsaybulin.network.models.CalendarItemDto

fun CalendarItemDto.asEntity() = CalendarItemDbo(
    nextEpisode = nextEpisode,
    nextEpisodeAt = nextEpisodeAt,
    duration = duration.takeIf { it != 0 },
    animeId = anime.id
)

fun CalendarItemDto.animeShell() = anime.asEntity()
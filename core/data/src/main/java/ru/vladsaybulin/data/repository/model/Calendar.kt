package ru.vladsaybulin.data.repository.model

import ru.vladsaybulin.database.models.CalendarItemDbo
import ru.vladsaybulin.network.retrofit.models.CalendarItemDto

fun CalendarItemDto.asDbo() = CalendarItemDbo(
    nextEpisode = nextEpisode,
    nextEpisodeAt = nextEpisodeAt,
    duration = duration.takeIf { it != 0 },
    animeId = anime.id
)

fun CalendarItemDto.animeShell() = anime.asDbo()
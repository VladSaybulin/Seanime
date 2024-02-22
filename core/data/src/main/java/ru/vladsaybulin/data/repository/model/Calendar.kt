package ru.vladsaybulin.data.repository.model

import ru.vladsaybulin.database.models.CalendarItemDbo
import ru.vladsaybulin.network.retrofit.models.CalendarDto

fun CalendarDto.asDbo() = CalendarItemDbo(
    nextEpisode = nextEpisode,
    nextEpisodeAt = nextEpisodeAt,
    duration = duration,
    animeId = anime.id
)

fun CalendarDto.animeShell() = anime.asDbo()
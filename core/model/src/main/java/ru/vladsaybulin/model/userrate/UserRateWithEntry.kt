package ru.vladsaybulin.model.userrate

import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.manga.Manga

data class UserRateWithEntry(
    val anime: Anime? = null,
    val manga: Manga? = null,
    val userRate: UserRate
)
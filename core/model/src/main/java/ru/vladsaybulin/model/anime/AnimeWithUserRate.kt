package ru.vladsaybulin.model.anime

import ru.vladsaybulin.model.userrate.UserRate

data class AnimeWithUserRate(
    val anime: Anime,
    val userRate: UserRate?
)
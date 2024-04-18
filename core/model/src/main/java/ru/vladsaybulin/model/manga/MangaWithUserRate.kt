package ru.vladsaybulin.model.manga

import ru.vladsaybulin.model.userrate.UserRate

data class MangaWithUserRate(
    val manga: Manga,
    val userRate: UserRate?
)
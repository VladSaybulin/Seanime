package ru.vladsaybulin.model.manga

import ru.vladsaybulin.model.Manga
import ru.vladsaybulin.model.UserRate

data class MangaWithUserRate(
    val manga: Manga,
    val userRate: UserRate?
)
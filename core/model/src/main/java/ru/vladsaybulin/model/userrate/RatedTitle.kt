package ru.vladsaybulin.model.userrate

import ru.vladsaybulin.model.anime.Anime as AnimeModel
import ru.vladsaybulin.model.manga.Manga as MangaModel

sealed interface RatedTitle {
    data class Anime(val anime: AnimeModel, override val userRate: UserRate): RatedTitle
    data class Manga(val manga: MangaModel, override val userRate: UserRate): RatedTitle

    val userRate: UserRate
}
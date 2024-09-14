package ru.vladsaybulin.model.related

import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.manga.Manga

sealed interface RelatedTitle {
    val relationType: RelationType
}

class RelatedAnime(val anime: Anime, override val relationType: RelationType) : RelatedTitle

class RelatedManga(val manga: Manga, override val relationType: RelationType) : RelatedTitle
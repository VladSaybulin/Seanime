package ru.vladsaybulin.model.topic

import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.manga.Manga

data class TopicLinkedEntry constructor(
    val anime: Anime? = null,
    val manga: Manga? = null
)
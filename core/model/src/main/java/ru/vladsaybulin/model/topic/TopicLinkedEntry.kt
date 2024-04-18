package ru.vladsaybulin.model.topic

import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.Manga

data class TopicLinkedEntry constructor(
    val anime: Anime? = null,
    val manga: Manga? = null
)
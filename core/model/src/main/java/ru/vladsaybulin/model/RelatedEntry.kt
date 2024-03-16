package ru.vladsaybulin.model

data class RelatedEntry(
    val anime: Anime? = null,
    val manga: Manga? = null,
    val relationType: RelationType
) {
    init {
        check(anime != null || manga != null) { "One of the parameters must be non-null" }
    }
}

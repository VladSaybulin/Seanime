package ru.vladsaybulin.model

data class Anime(
    val id: Long,
    val originalName: String,
    val russianName: String,
    val poster: Poster?,
    val kind: AnimeKind,
    val status: EntryStatus,
    val score: Float,
    val episodes: Int,
    val episodesAired: Int,
    val airedOn: IncompleteDate,
    val releasedOn: IncompleteDate
)
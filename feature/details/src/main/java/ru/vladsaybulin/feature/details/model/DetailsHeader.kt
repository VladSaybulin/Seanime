package ru.vladsaybulin.feature.details.model

import ru.vladsaybulin.model.AnimeRating
import ru.vladsaybulin.model.Poster

data class DetailsHeader(
    val poster: Poster?,
    val name: String,
    val russianName: String?,
    val animeRating: AnimeRating?
)
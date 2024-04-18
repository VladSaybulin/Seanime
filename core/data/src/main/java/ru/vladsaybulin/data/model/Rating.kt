package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.network.graphql.type.AnimeRatingEnum
import ru.vladsaybulin.model.anime.AnimeRating

fun AnimeRatingEnum?.asAnimeRating() = when (this) {
    AnimeRatingEnum.g -> AnimeRating.G
    AnimeRatingEnum.pg -> AnimeRating.PG
    AnimeRatingEnum.pg_13 -> AnimeRating.PG13
    AnimeRatingEnum.r -> AnimeRating.R
    AnimeRatingEnum.r_plus -> AnimeRating.RPlus
    AnimeRatingEnum.rx -> AnimeRating.RX
    else -> AnimeRating.None
}
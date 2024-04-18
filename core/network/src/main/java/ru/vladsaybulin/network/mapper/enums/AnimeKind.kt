package ru.vladsaybulin.network.mapper.enums

import ru.vladsaybulin.core.network.graphql.type.AnimeKindEnum
import ru.vladsaybulin.model.anime.AnimeKind

fun AnimeKindEnum?.asAnimeKind() = when (this) {
    AnimeKindEnum.tv -> AnimeKind.Tv
    AnimeKindEnum.movie -> AnimeKind.Movie
    AnimeKindEnum.ova -> AnimeKind.Ova
    AnimeKindEnum.ona -> AnimeKind.Ona
    AnimeKindEnum.special -> AnimeKind.Special
    AnimeKindEnum.tv_special -> AnimeKind.TvSpecial
    AnimeKindEnum.music -> AnimeKind.Music
    AnimeKindEnum.pv -> AnimeKind.Pv
    AnimeKindEnum.cm -> AnimeKind.Cm
    else -> AnimeKind.None
}
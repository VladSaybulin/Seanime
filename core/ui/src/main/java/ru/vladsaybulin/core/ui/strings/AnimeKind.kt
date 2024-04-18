package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.anime.AnimeKind

fun animeKindStringResId(kind: AnimeKind): Int? = when (kind) {
    AnimeKind.Tv -> R.string.anime_kind_tv
    AnimeKind.Movie -> R.string.anime_kind_movie
    AnimeKind.Ona -> R.string.anime_kind_ona
    AnimeKind.Ova -> R.string.anime_kind_ova
    AnimeKind.Music -> R.string.anime_kind_music
    AnimeKind.Special -> R.string.anime_kind_special
    AnimeKind.Pv -> R.string.anime_kind_pv
    AnimeKind.Cm -> R.string.anime_kind_cv
    AnimeKind.TvSpecial -> R.string.anime_kind_tv_special
    AnimeKind.None -> null
}

@Composable
@ReadOnlyComposable
fun animeKindString(animeKind: AnimeKind): String? =
    animeKindStringResId(animeKind)?.let { stringResource(id = it) }
package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.AnimeRating

fun animeRatingStringId(animeRating: AnimeRating) = when (animeRating) {
    AnimeRating.G -> R.string.core_ui_anime_rating_g
    AnimeRating.PG -> R.string.core_ui_anime_rating_pg
    AnimeRating.PG13 -> R.string.core_ui_anime_rating_pg13
    AnimeRating.R -> R.string.core_ui_anime_rating_r
    AnimeRating.RPlus -> R.string.core_ui_anime_rating_r_plus
    AnimeRating.RX -> R.string.core_ui_anime_rating_rx
    AnimeRating.None -> null
}

@Composable
@ReadOnlyComposable
fun animeRatingString(animeRating: AnimeRating) =
    animeRatingStringId(animeRating = animeRating)
        ?.let { stringResource(id = it) }
        ?: animeRating.name
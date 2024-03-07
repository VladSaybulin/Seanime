package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.EntryStatus

@Composable
@ReadOnlyComposable
fun animeKindStringResource(kind: AnimeKind): String? = when (kind) {
    AnimeKind.Tv -> stringResource(id = R.string.anime_kind_tv)
    AnimeKind.Movie -> stringResource(id = R.string.anime_kind_movie)
    AnimeKind.Ona -> stringResource(id = R.string.anime_kind_ona)
    AnimeKind.Ova -> stringResource(id = R.string.anime_kind_ova)
    AnimeKind.Music -> stringResource(id = R.string.anime_kind_music)
    AnimeKind.Special -> stringResource(id = R.string.anime_kind_special)
    AnimeKind.Pv -> stringResource(id = R.string.anime_kind_pv)
    AnimeKind.Cm -> stringResource(id = R.string.anime_kind_cv)
    AnimeKind.None -> null
}

@Composable
@ReadOnlyComposable
fun animeStatusStringResource(status: EntryStatus) = when (status) {
    EntryStatus.Ongoing -> stringResource(id = R.string.anime_status_ongoing)
    EntryStatus.Released -> stringResource(id = R.string.anime_status_released)
    EntryStatus.Anons -> stringResource(id = R.string.anime_status_anons)
    else -> null
}
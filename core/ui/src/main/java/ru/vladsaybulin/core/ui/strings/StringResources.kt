package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.UserRateStatus

@Composable
@ReadOnlyComposable
fun animeKindString(animeKind: AnimeKind): String? =
    animeKindStringResId(animeKind)?.let { stringResource(id = it) }

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
fun animeStatusStringResource(status: EntryStatus) = when (status) {
    EntryStatus.Ongoing -> stringResource(id = R.string.anime_status_ongoing)
    EntryStatus.Released -> stringResource(id = R.string.anime_status_released)
    EntryStatus.Anons -> stringResource(id = R.string.anime_status_anons)
    else -> null
}

@Composable
@ReadOnlyComposable
fun animeUserRateStatusString(userRateStatus: UserRateStatus) =
    animeUserRateStatusStringResId(userRateStatus)?.let { stringResource(id = it) }

fun animeUserRateStatusStringResId(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> R.string.user_rate_status_planned
    UserRateStatus.Watching -> R.string.user_rate_status_watching
    UserRateStatus.Rewatching -> R.string.user_rate_status_rewatching
    UserRateStatus.Completed -> R.string.user_rate_status_completed
    UserRateStatus.OnHold -> R.string.user_rate_status_on_hold
    UserRateStatus.Dropped -> R.string.user_rate_status_dropped
    UserRateStatus.None -> null
}
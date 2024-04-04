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
fun buildEpisodesString(
    status: EntryStatus,
    episodes: Int,
    episodesAired: Int,
    duration: Int?
): String? {

    val episodesText = when {
        episodes == 1 -> null //Not show single episode
        episodes > 1 -> episodes.toString()
        episodesAired > 0 -> "-" //Show episodes as unknown
        else -> null //Not show episodes
    }

    if (episodesText == null && duration == null) return null

    val episodesAiredText = episodesAired.takeIf { status == EntryStatus.Ongoing }?.toString()

    val durationText: String? = null

    return when {
        episodesText != null && episodesAiredText != null ->
            if (durationText != null) {
                stringResource(
                    id = R.string.aired_of_episodes_with_duration,
                    episodesAiredText,
                    episodesText,
                    durationText
                )
            } else {
                stringResource(
                    id = R.string.aired_of_episodes,
                    episodesAiredText,
                    episodesText,
                )
            }

        episodesText != null ->
            if (durationText != null) {
                stringResource(
                    id = R.string.episodes_with_duration,
                    episodesText,
                    durationText
                )
            } else {
                stringResource(
                    id = R.string.episodes,
                    episodesText,
                )
            }

        else -> durationText
    }
}

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

fun animeStatusStringResId(status: EntryStatus) = when (status) {
    EntryStatus.Anons ->  R.string.anime_status_anons
    EntryStatus.Ongoing -> R.string.anime_status_ongoing
    EntryStatus.Released ->R.string.anime_status_released
    else -> null
}

@Composable
@ReadOnlyComposable
fun animeStatusString(status: EntryStatus) =
    animeStatusStringResId(status)?.let { stringResource(id = it) }

fun animeUserRateStatusStringResId(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> R.string.user_rate_status_planned
    UserRateStatus.Watching -> R.string.user_rate_status_watching
    UserRateStatus.Rewatching -> R.string.user_rate_status_rewatching
    UserRateStatus.Completed -> R.string.user_rate_status_completed
    UserRateStatus.OnHold -> R.string.user_rate_status_on_hold
    UserRateStatus.Dropped -> R.string.user_rate_status_dropped
    UserRateStatus.None -> null
}

@Composable
@ReadOnlyComposable
fun animeUserRateStatusString(userRateStatus: UserRateStatus) =
    animeUserRateStatusStringResId(userRateStatus)?.let { stringResource(id = it) }

@Composable
@ReadOnlyComposable
fun notNoneAnimeUserRateStatusString(userRateStatus: UserRateStatus) =
    stringResource(
        id = requireNotNull(animeUserRateStatusStringResId(userRateStatus)) {
            "UserRateStatus is None"
        }
    )



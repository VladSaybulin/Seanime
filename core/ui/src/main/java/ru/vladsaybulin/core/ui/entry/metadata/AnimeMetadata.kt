package ru.vladsaybulin.core.ui.entry.metadata

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.strings.animeKindString
import ru.vladsaybulin.core.ui.strings.animeKindStringResId
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.common.EntryStatus

@Composable
fun DefaultAnimeGridItemMetadata(anime: Anime) {
    AnimeMetadataDefaultComponents.KindAndYear(anime = anime)
}

@Composable
fun DefaultAnimeListItemMetadata(anime: Anime) {
    Column {
        AnimeMetadataDefaultComponents.KindAndEpisodes(
            kind = anime.kind,
            episodes = anime.episodes,
            episodesAired = anime.episodesAired,
            duration = 0,
            isOngoing = anime.status == EntryStatus.Ongoing
        )
    }
}

object AnimeMetadataDefaultComponents {

    @Composable
    fun KindAndYear(anime: Anime) {
        TitleMetadataComponents.KindAndYearLine(
            kindStringId = animeKindStringResId(kind = anime.kind),
            year = anime.airedOn?.year ?: anime.releasedOn?.year
        )
    }

    @Composable
    fun KindAndEpisodes(
        kind: AnimeKind,
        episodes: Int,
        episodesAired: Int,
        duration: Int,
        isOngoing: Boolean
    ) {
        val isSingleEpisode = episodes == 1 || (!isOngoing && episodes == 0)

        val episodesText = when {
            isSingleEpisode -> null
            !isOngoing -> stringResource(id = R.string.core_ui_anime_info_episodes, episodes)
            episodes > 0 && episodesAired > 0 -> stringResource(
                id = R.string.core_ui_anime_info_episodes_of_episodes,
                episodesAired,
                episodes
            )

            episodesAired > 0 -> stringResource(
                id = R.string.core_ui_anime_info_episodes_of_unknown_episodes,
                episodesAired
            )

            else -> null
        }

        val durationText = if (duration > 0) {
            val durationHours = duration / 60
            val durationMinutes = duration % 60

            val durationHoursText = if (durationHours > 0) {
                pluralStringResource(
                    id = if (isSingleEpisode) {
                        R.plurals.core_ui_anime_info_duration_hours
                    } else R.plurals.core_ui_anime_info_by_duration_hours,
                    count = durationHours,
                    durationHours
                )
            } else null

            val durationMinutesText = pluralStringResource(
                id = if (isSingleEpisode) {
                    R.plurals.core_ui_anime_info_duration_minutes
                } else R.plurals.core_ui_anime_info_by_duration_minutes,
                count = durationMinutes,
                durationMinutes
            )

            when {
                isSingleEpisode && durationHoursText != null -> stringResource(
                    id = R.string.core_ui_anime_info_duration_hours_minutes,
                    durationHoursText,
                    durationMinutes
                )

                durationHoursText != null -> stringResource(
                    id = R.string.core_ui_anime_info_by_duration_hours_minutes,
                    durationHoursText,
                    durationMinutes
                )

                !isSingleEpisode -> stringResource(
                    id = R.string.core_ui_anime_info_by_duration_minutes,
                    durationMinutesText
                )

                else -> durationMinutesText
            }
        } else null

        val kindText = if (kind != AnimeKind.None) animeKindString(kind) else null

        if (kindText != null || episodesText != null || durationText != null) {
            val separator = stringResource(id = R.string.core_ui_info_separator)
            Text(
                text = listOfNotNull(
                    kindText,
                    episodesText,
                    durationText
                ).joinToString(separator = separator)
            )
        }
    }
}
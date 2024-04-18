package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.common.EntryStatus

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



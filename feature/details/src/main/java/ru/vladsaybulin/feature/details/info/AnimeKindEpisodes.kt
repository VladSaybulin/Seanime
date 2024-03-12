package ru.vladsaybulin.feature.details.info

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.strings.animeKindString
import ru.vladsaybulin.feature.details.model.DetailsInfo
import ru.vladsaybulin.model.AnimeKind

@Composable
internal fun AnimeKindEpisodeLine(
    info: DetailsInfo.AnimeKindEpisodes,
    modifier: Modifier = Modifier
) {
    val text = buildKindAndEpisodesString(
        kind = info.kind,
        episodes = info.episodes,
        episodesAired = info.episodesAired,
        duration = info.duration,
        ongoing = info.ongoing
    ) ?: return

    InfoLine(
        icon = { InfoIcon(imageVector = ShikimoriIcons.Tv) },
        modifier = modifier
    ) {
        Text(text)
    }
}


@Composable
@ReadOnlyComposable
private fun buildKindAndEpisodesString(
    kind: AnimeKind,
    episodes: Int,
    episodesAired: Int,
    duration: Int?,
    ongoing: Boolean
): String? {
    val kindText = animeKindString(animeKind = kind)
    val episodesText = buildEpisodesString(
        episodes = episodes,
        episodesAired = episodesAired,
        ongoing = ongoing
    )
    val durationText = duration?.let {
        buildDurationString(it)
    }

    if (kindText == null && episodesText == null && durationText == null) return null

    return buildString {
        kindText?.let { append(it) }

        if (episodesText == null && durationText == null) return@buildString

        if (isNotEmpty()) append(", ")
        when {
            episodesText != null && durationText != null -> stringResource(
                id = R.string.episodes_x_duration,
                episodesText,
                durationText
            ).let { append(it) }

            episodesText != null -> stringResource(
                id = R.string.episodes,
                episodesText
            ).let { append(it) }

            else -> append(durationText)
        }
    }
}

@Composable
@ReadOnlyComposable
private fun buildDurationString(duration: Int): String {
    val hours = duration / 60
    val minutes = duration % 60

    val hoursText = hours.takeIf { it > 0 }?.let {
        pluralStringResource(id = R.plurals.duration_hours, count = it, it)
    }
    val minutesText = stringResource(id = R.string.duration_minutes, minutes)

    return buildString {
        hoursText?.let { append(it) }
        if (isNotEmpty()) append(' ')
        append(minutesText)
    }
}

@Composable
@ReadOnlyComposable
private fun buildEpisodesString(episodes: Int, episodesAired: Int, ongoing: Boolean): String? {
    if (episodes <= 0 && episodesAired <= 0) return null

    return when {
        ongoing && episodes > 0 && episodesAired > 0 -> "$episodesAired/$episodes"
        ongoing && episodesAired > 0 -> "$episodesAired/-"
        episodes > 0 -> episodes.toString()
        else -> null
    }
}

@Preview
@Composable
fun DetailsInfoAnimeKindEpisodeLinePreview(
    @PreviewParameter(DetailsInfoAnimeKindEpisodeLinePreviewProvider::class) info: DetailsInfo.AnimeKindEpisodes
) {
    ShikimoriTheme {
        Surface {
            AnimeKindEpisodeLine(info = info)
        }
    }
}

class DetailsInfoAnimeKindEpisodeLinePreviewProvider : PreviewParameterProvider<DetailsInfo.AnimeKindEpisodes> {
    override val values: Sequence<DetailsInfo.AnimeKindEpisodes> = sequenceOf(
        DetailsInfo.AnimeKindEpisodes(
            kind = AnimeKind.Tv,
            episodes = 24,
            episodesAired = 22,
            duration = 21,
            ongoing = true
        ),
        DetailsInfo.AnimeKindEpisodes(
            kind = AnimeKind.Tv,
            episodes = 24,
            episodesAired = 0,
            duration = 21,
            ongoing = false
        ),
        DetailsInfo.AnimeKindEpisodes(
            kind = AnimeKind.Tv,
            episodes = 24,
            episodesAired = 24,
            duration = null,
            ongoing = false
        ),
        DetailsInfo.AnimeKindEpisodes(
            kind = AnimeKind.None,
            episodes = 24,
            episodesAired = 0,
            duration = 67,
            ongoing = false
        )
    )
}
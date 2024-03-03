package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.strings.animeKindStringResource
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.EntryStatus

@Composable
fun AnimeListItem(
    modifier: Modifier = Modifier,
    anime: Anime,
    onClick: () -> Unit,
    detailsContent: (@Composable () -> Unit)? = { DefaultAnimeDetails(anime) },
) {
    EntryListItem(
        modifier = modifier,
        name = anime.russianName ?: anime.originalName,
        poster = anime.poster,
        onClick = onClick,
        detailsContent = detailsContent
    )
}

@Composable
private fun DefaultAnimeDetails(anime: Anime) {
    EntryListItemDetails(data = anime.listItemDetailsData())
}

@Composable
@ReadOnlyComposable
fun Anime.listItemDetailsData() = EntryListItemDetailsData(
    kindText = animeKindStringResource(kind = kind),
    year = airedOn?.year ?: releasedOn?.year,
    entryStatus = status,
    volumeText = buildEpisodesText(
        kind = kind,
        status = status,
        episodes = episodes,
        episodesAired = episodesAired
    ),
    score = score
)

@Composable
@ReadOnlyComposable
private fun buildEpisodesText(
    kind: AnimeKind,
    status: EntryStatus,
    episodes: Int,
    episodesAired: Int
): String? {
    if (kind in KindsWithoutEpisodesText) return null

    val episodesText: String = when {
        episodes > 0 -> episodes.toString()
        episodesAired > 0 -> "-"
        else -> return null
    }

    return when {
        status == EntryStatus.Ongoing && episodesAired > 0 -> stringResource(
            id = R.string.aired_of_episodes,
            episodesText,
            episodesAired.toString()
        )

        else -> stringResource(
            id = R.string.episodes,
            episodes.toString()
        )
    }
}

@Preview
@Composable
fun AnimeListItemPreview(@PreviewParameter(AnimePreviewProvider::class) anime: Anime) {
    ShikimoriTheme {
        AnimeListItem(
            modifier = Modifier.width(360.dp),
            anime = anime,
            onClick = { }
        )
    }
}

private val KindsWithoutEpisodesText = sequenceOf(
    AnimeKind.Movie,
    AnimeKind.Music,
    AnimeKind.Cv,
    AnimeKind.Pv
)

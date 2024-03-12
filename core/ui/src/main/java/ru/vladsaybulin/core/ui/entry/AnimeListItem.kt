package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.strings.animeKindString
import ru.vladsaybulin.core.ui.strings.buildEpisodesString
import ru.vladsaybulin.model.Anime

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
    kindText = animeKindString(animeKind = kind),
    year = airedOn?.year ?: releasedOn?.year,
    entryStatus = status,
    volumeText = buildEpisodesString(
        status = status,
        episodes = episodes,
        episodesAired = episodesAired,
        duration = null
    ),
    score = score
)

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


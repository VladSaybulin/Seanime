package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.strings.animeKindStringResource
import ru.vladsaybulin.model.Anime

@Composable
fun AnimeGridItem(
    modifier: Modifier = Modifier,
    anime: Anime,
    onClick: () -> Unit,
    detailsContent: (@Composable () -> Unit)? = { DefaultAnimeDetails(anime) },
) {
    EntryGridItem(
        modifier = modifier,
        name = anime.russianName ?: anime.originalName,
        poster = anime.poster,
        onClick = onClick,
        detailsContent = detailsContent
    )
}

@Composable
fun SmallAnimeGridItem(
    modifier: Modifier = Modifier,
    anime: Anime,
    onClick: () -> Unit,
    detailsContent: (@Composable () -> Unit)? = null,
) {
    SmallEntryGridItem(
        modifier = modifier,
        name = anime.russianName ?: anime.originalName,
        poster = anime.poster,
        onClick = onClick,
        detailsContent = detailsContent
    )
}

@Composable
private fun DefaultAnimeDetails(anime: Anime) {
    EntryGridItemDetails(data = anime.gridItemDetailsData())
}

@Composable
@ReadOnlyComposable
private fun Anime.gridItemDetailsData() = EntryGridItemDetailsData(
    kindText = animeKindStringResource(kind = kind),
    year = airedOn?.year ?: releasedOn?.year
)

@Preview
@Composable
fun AnimeGridItemPreview(@PreviewParameter(AnimePreviewProvider::class) anime: Anime) {
    ShikimoriTheme {
        AnimeGridItem(
            anime = anime,
            onClick = { },
            modifier = Modifier.width(150.dp)
        )
    }
}

@Preview
@Composable
fun SmallAnimeGridItemPreview(@PreviewParameter(AnimePreviewProvider::class) anime: Anime) {
    ShikimoriTheme {
        SmallAnimeGridItem(
            anime = anime,
            onClick = { },
            modifier = Modifier.width(96.dp)
        )
    }
}
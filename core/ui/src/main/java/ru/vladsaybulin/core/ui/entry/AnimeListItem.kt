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
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.related.RelationType

@Composable
fun AnimeListItem(
    modifier: Modifier = Modifier,
    anime: Anime,
    relationType: RelationType? = null,
    onClick: () -> Unit,
    detailsContent: (@Composable () -> Unit)? = { DefaultAnimeDetails(anime, relationType) },
) {
    EntryListItem(
        modifier = modifier,
        name = anime.russianName ?: anime.name,
        poster = anime.poster,
        onClick = onClick,
        detailsContent = detailsContent
    )
}

@Composable
private fun DefaultAnimeDetails(anime: Anime, relationType: RelationType? = null) {
    EntryListItemDetails(data = anime.listItemDetailsData(relationType))
}

@Composable
@ReadOnlyComposable
fun Anime.listItemDetailsData(relationType: RelationType? = null) = EntryListItemDetailsData(
    kindText = animeKindString(animeKind = kind),
    year = airedOn?.year ?: releasedOn?.year,
    entryStatus = status,
    volumeText = buildEpisodesString(
        status = status,
        episodes = episodes,
        episodesAired = episodesAired,
        duration = null
    ),
    score = score,
    relationType = relationType
)

@Preview
@Composable
fun AnimeListItemPreview(@PreviewParameter(AnimePreviewProvider::class) anime: Anime) {
    ShikimoriTheme {
        AnimeListItem(
            modifier = Modifier.width(360.dp),
            anime = anime,
            relationType = RelationType.AltHistory,
            onClick = { }
        )
    }
}


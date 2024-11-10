package ru.vladsaybulin.core.ui.entry.grid.anime

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.entry.preview.AnimePreviewProvider
import ru.vladsaybulin.core.ui.entry.grid.EntryGridItem
import ru.vladsaybulin.core.ui.entry.metadata.AnimeMetadataDefaultComponents
import ru.vladsaybulin.core.ui.entry.metadata.DefaultAnimeGridItemMetadata
import ru.vladsaybulin.core.ui.strings.animeKindStringResId
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun AnimeGridItem(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    metadata: (@Composable ColumnScope.() -> Unit)? = { DefaultAnimeGridItemMetadata(anime) },
) {
    EntryGridItem(
        modifier = modifier,
        name = anime.russianName ?: anime.name,
        userRateStatus = userRateStatus,
        imageUrl = anime.poster?.previewUrl,
        onClick = onClick,
        metadata = metadata
    )
}

@Preview
@Composable
fun AnimeGridItemPreview(@PreviewParameter(AnimePreviewProvider::class) anime: Anime) {
    SeanimeTheme {
        AnimeGridItem(
            anime = anime,
            onClick = { },
            modifier = Modifier.width(150.dp)
        )
    }
}
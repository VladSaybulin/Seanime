package ru.vladsaybulin.core.ui.anime

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.entry.AnimePreviewProvider
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.core.ui.entry.EntryKindAndYearMetadata
import ru.vladsaybulin.core.ui.strings.animeKindString
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun AnimeGridItem(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    detailsContent: (@Composable () -> Unit)? = { AnimeGridMetadata(anime) },
) {
    EntryGridItem(
        modifier = modifier,
        name = anime.russianName ?: anime.name,
        userRateStatus = userRateStatus,
        poster = anime.poster,
        onClick = onClick,
        detailsContent = detailsContent
    )
}

@Composable
fun AnimeGridMetadata(anime: Anime) {
    EntryKindAndYearMetadata(
        entryKindString = animeKindString(anime.kind),
        airedInYear = anime.airedOn?.year
    )
}

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
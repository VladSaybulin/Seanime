package ru.vladsaybulin.core.ui.anime

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeWithUserRate
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun AnimeGridItem(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    metadata: (@Composable ColumnScope.() -> Unit)? = { AnimeGridMetadata(anime) },
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

@Composable
fun AnimeGridItem(
    animeWithUserRate: AnimeWithUserRate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    metadata: (@Composable ColumnScope.() -> Unit)? = { AnimeGridMetadata(animeWithUserRate.anime) },
) {
    val anime = animeWithUserRate.anime

    EntryGridItem(
        modifier = modifier,
        name = anime.russianName ?: anime.name,
        userRateStatus = animeWithUserRate.userRate?.status ?: UserRateStatus.None,
        imageUrl = anime.poster?.previewUrl,
        onClick = onClick,
        metadata = metadata
    )
}

@Composable
fun AnimeGridMetadata(anime: Anime) {
    AnimeInfoKindAndYearText(
        kind = anime.kind,
        year = anime.airedOn?.year
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
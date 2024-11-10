package ru.vladsaybulin.core.ui.entry.list.anime

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.ui.entry.list.EntryListItem
import ru.vladsaybulin.core.ui.entry.metadata.DefaultAnimeListItemMetadata
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun AnimeListItem(
    anime: Anime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    metadata: (@Composable ColumnScope.() -> Unit)? = { DefaultAnimeListItemMetadata(anime = anime) },
) {
    EntryListItem(
        name = anime.russianName ?: anime.name,
        userRateStatus = userRateStatus,
        imageUrl = anime.poster?.previewUrl,
        onClick = onClick,
        modifier = modifier,
        metadata = metadata
    )
}
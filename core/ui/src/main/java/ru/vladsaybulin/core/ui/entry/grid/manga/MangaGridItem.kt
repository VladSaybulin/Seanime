package ru.vladsaybulin.core.ui.entry.grid.manga

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.ui.entry.grid.EntryGridItem
import ru.vladsaybulin.core.ui.entry.metadata.MangaMetadataComponents
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun MangaGridItem(
    manga: Manga,
    onClick: () -> Unit,
    modifier: Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    metadata: (@Composable ColumnScope.() -> Unit)? = { MangaMetadataComponents.KindAndYearLine(manga) },
) {
    EntryGridItem(
        modifier = modifier,
        name = manga.russianName ?: manga.name,
        userRateStatus = userRateStatus,
        imageUrl = manga.poster?.previewUrl,
        onClick = onClick,
        metadata = metadata
    )
}
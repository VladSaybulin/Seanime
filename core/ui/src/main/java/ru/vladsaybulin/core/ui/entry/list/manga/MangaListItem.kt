package ru.vladsaybulin.core.ui.entry.list.manga

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.ui.entry.list.EntryListItem
import ru.vladsaybulin.core.ui.entry.metadata.DefaultMangaListItemMetadata
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun MangaListItem(
    manga: Manga,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    metadata: (@Composable ColumnScope.() -> Unit)? = { DefaultMangaListItemMetadata(manga) },
) {
    EntryListItem(
        name = manga.russianName ?: manga.name,
        userRateStatus = userRateStatus,
        imageUrl = manga.poster?.previewUrl,
        onClick = onClick,
        modifier = modifier,
        metadata = metadata
    )
}
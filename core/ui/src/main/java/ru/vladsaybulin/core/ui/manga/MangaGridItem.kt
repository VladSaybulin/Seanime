package ru.vladsaybulin.core.ui.manga

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.core.ui.entry.EntryInfoKindAndYear
import ru.vladsaybulin.core.ui.strings.mangaKindString
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun MangaGridItem(
    manga: Manga,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    metadata: (@Composable ColumnScope.() -> Unit)? = { MangaGridMetadata(manga) },
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

@Composable
fun MangaGridMetadata(manga: Manga) {
    EntryInfoKindAndYear(
        kindText = if (manga.kind != MangaKind.None) mangaKindString(manga.kind) else null,
        year = manga.airedOn?.year
    )
}
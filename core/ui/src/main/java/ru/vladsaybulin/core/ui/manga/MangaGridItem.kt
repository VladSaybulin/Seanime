package ru.vladsaybulin.core.ui.manga

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.core.ui.entry.EntryKindAndYearMetadata
import ru.vladsaybulin.core.ui.strings.mangaKindString
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun MangaGridItem(
    manga: Manga,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    detailsContent: (@Composable () -> Unit)? = { MangaGridMetadata(manga) },
) {
    EntryGridItem(
        modifier = modifier,
        name = manga.russianName ?: manga.name,
        userRateStatus = userRateStatus,
        poster = manga.poster,
        onClick = onClick,
        detailsContent = detailsContent
    )
}

@Composable
fun MangaGridMetadata(manga: Manga) {
    EntryKindAndYearMetadata(
        entryKindString = mangaKindString(mangaKind = manga.kind),
        airedInYear = manga.airedOn?.year
    )
}
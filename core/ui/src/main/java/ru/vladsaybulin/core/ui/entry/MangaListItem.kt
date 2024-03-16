package ru.vladsaybulin.core.ui.entry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.ui.strings.mangaKindString
import ru.vladsaybulin.core.ui.strings.volumesAndChaptersString
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Manga
import ru.vladsaybulin.model.RelationType

@Composable
fun MangaListItem(
    manga: Manga,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    relationType: RelationType? = null,
    detailsContent: @Composable () -> Unit = {
        DefaultMangaDetailsContent(manga, relationType)
    }
) {
    EntryListItem(
        name = manga.run { russianName ?: originalName },
        poster = manga.poster,
        onClick = onClick,
        modifier = modifier,
        detailsContent = detailsContent
    )
}

@Composable
fun DefaultMangaDetailsContent(manga: Manga, relationType: RelationType? = null) {
    EntryListItemDetails(data = manga.listItemDetailsData(relationType))
}

@Composable
fun Manga.listItemDetailsData(relationType: RelationType? = null) = EntryListItemDetailsData(
    kindText = mangaKindString(kind),
    year = airedOn?.year,
    entryStatus = status,
    volumeText = volumesAndChaptersString(volumes = volumes, chapters = chapters),
    score = score,
    relationType = relationType,
    entryType = EntryType.Manga
)
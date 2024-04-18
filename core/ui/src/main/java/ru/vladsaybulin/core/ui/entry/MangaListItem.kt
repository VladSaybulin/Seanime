package ru.vladsaybulin.core.ui.entry

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.ui.strings.chaptersAndVolumesString
import ru.vladsaybulin.core.ui.strings.mangaKindString
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.related.RelationType

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
        name = manga.run { russianName ?: name },
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
    volumeText = chaptersAndVolumesString(volumes = volumes, chapters = chapters),
    score = score,
    relationType = relationType
)
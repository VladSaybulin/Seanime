package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.ui.entry.EntryListItem
import ru.vladsaybulin.core.ui.entry.EntryListItemDetails
import ru.vladsaybulin.core.ui.entry.listItemDetailsData
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Manga
import ru.vladsaybulin.model.RelatedEntry
import ru.vladsaybulin.model.RelationType

@Composable
fun RelatedEntryListItem(
    relatedEntry: RelatedEntry,
    onEntryClick: (EntryType, Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (relatedEntry.anime != null) {
        val anime = relatedEntry.anime!!
        RelatedAnimeListItem(
            anime = anime,
            relationType = relatedEntry.relationType,
            onClick = { onEntryClick(EntryType.Anime, anime.id) },
            modifier = modifier
        )
    } else {
        val manga = relatedEntry.manga!!
        RelatedMangaListItem(
            manga = manga,
            relationType = relatedEntry.relationType,
            onClick = { onEntryClick(EntryType.Manga, manga.id) },
            modifier = modifier
        )
    }
}

@Composable
private fun RelatedAnimeListItem(
    anime: Anime,
    relationType: RelationType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EntryListItem(
        name = anime.run { russianName ?: originalName },
        poster = anime.poster,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        posterWidth = 72.dp,
        detailsContent = {
            EntryListItemDetails(
                data = anime.listItemDetailsData()
                    .copy(
                        volumeText = null,
                        relationType = relationType
                    )
            )
        }
    )
}

@Composable
private fun RelatedMangaListItem(
    manga: Manga,
    relationType: RelationType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EntryListItem(
        name = manga.run { russianName ?: originalName },
        poster = manga.poster,
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        posterWidth = 72.dp,
        detailsContent = {
            EntryListItemDetails(
                data = manga.listItemDetailsData().copy(
                    volumeText = null,
                    relationType = relationType
                )
            )
        }
    )
}
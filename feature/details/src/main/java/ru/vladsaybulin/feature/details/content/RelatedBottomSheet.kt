package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriModalBottomSheet
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.related.RelatedEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RelatedBottomSheet(
    related: List<RelatedEntry>,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    ShikimoriModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        RelatedBottomSheetContent(
            related = related,
            onAnimeClick = onAnimeClick,
            onMangaClick = onMangaClick
        )
    }
}

@Composable
private fun RelatedBottomSheetContent(
    related: List<RelatedEntry>,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = WindowInsets.navigationBars.asPaddingValues()
    ) {
        items(items = related) {
            RelatedEntryListItem(
                relatedEntry = it,
                onAnimeClick = onAnimeClick,
                onMangaClick = onMangaClick
            )
        }
    }
}
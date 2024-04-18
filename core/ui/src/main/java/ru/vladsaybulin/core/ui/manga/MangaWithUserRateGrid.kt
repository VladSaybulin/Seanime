package ru.vladsaybulin.core.ui.manga

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import ru.vladsaybulin.core.ui.entry.EntryGrid
import ru.vladsaybulin.core.ui.entry.EntryGridDefaults
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.manga.MangaWithUserRate

@Composable
fun MangaWithUserRateGrid(
    items: LazyPagingItems<MangaWithUserRate>,
    onEntryClick: (MangaWithUserRate) -> Unit,
    modifier: Modifier = Modifier,
    key: ((MangaWithUserRate) -> Any)? = { it.manga.id },
    columns: GridCells = EntryGridDefaults.DefaultColumns,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = EntryGridDefaults.DefaultContentPadding,
    horizontalArrangement: Arrangement.Horizontal = EntryGridDefaults.DefaultHorizontalArrangement,
    verticalArrangement: Arrangement.Vertical = EntryGridDefaults.DefaultVerticalArrangement,
    detailsContent: (@Composable (MangaWithUserRate) -> Unit)? = { MangaGridMetadata(it.manga) }
) {
    EntryGrid(
        items = items,
        modifier = modifier,
        key = key,
        columns = columns,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) { mangaWithUserRate ->
        MangaGridItem(
            manga = mangaWithUserRate.manga,
            onClick = { onEntryClick(mangaWithUserRate) },
            modifier = Modifier.fillMaxWidth(),
            userRateStatus = mangaWithUserRate.userRate?.status ?: UserRateStatus.None,
            detailsContent = if (detailsContent != null) {
                { detailsContent(mangaWithUserRate) }
            } else null
        )
    }
}
package ru.vladsaybulin.core.ui.anime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
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
import ru.vladsaybulin.model.anime.AnimeWithUserRate
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun AnimeGrid(
    items: List<AnimeWithUserRate>,
    onEntryClick: (AnimeWithUserRate) -> Unit,
    modifier: Modifier = Modifier,
    key: ((AnimeWithUserRate) -> Any)? = { it.anime.id },
    columns: GridCells = EntryGridDefaults.DefaultColumns,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = EntryGridDefaults.DefaultContentPadding,
    horizontalArrangement: Arrangement.Horizontal = EntryGridDefaults.DefaultHorizontalArrangement,
    verticalArrangement: Arrangement.Vertical = EntryGridDefaults.DefaultVerticalArrangement,
    metadata: (@Composable ColumnScope.(AnimeWithUserRate) -> Unit)? = { AnimeGridMetadata(it.anime) }
) {
    EntryGrid(
        items = items,
        modifier = modifier,
        key = key,
        columns = columns,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
    ) { animeWithUserRate ->
        AnimeGridItem(
            animeWithUserRate = animeWithUserRate,
            onClick = { onEntryClick(animeWithUserRate) },
            modifier = Modifier.fillMaxWidth(),
            metadata = if (metadata != null) {
                { metadata(animeWithUserRate) }
            } else null
        )
    }
}

@Composable
fun AnimeGrid(
    items: LazyPagingItems<AnimeWithUserRate>,
    onEntryClick: (AnimeWithUserRate) -> Unit,
    modifier: Modifier = Modifier,
    key: ((AnimeWithUserRate) -> Any)? = { it.anime.id },
    columns: GridCells = EntryGridDefaults.DefaultColumns,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = EntryGridDefaults.DefaultContentPadding,
    horizontalArrangement: Arrangement.Horizontal = EntryGridDefaults.DefaultHorizontalArrangement,
    verticalArrangement: Arrangement.Vertical = EntryGridDefaults.DefaultVerticalArrangement,
    metadata: (@Composable ColumnScope.(AnimeWithUserRate) -> Unit)? = { AnimeGridMetadata(it.anime) }
) {
    EntryGrid(
        items = items,
        modifier = modifier,
        key = key,
        columns = columns,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
    ) { animeWithUSerRate ->
        AnimeGridItem(
            anime = animeWithUSerRate.anime,
            onClick = { onEntryClick(animeWithUSerRate) },
            modifier = Modifier.fillMaxWidth(),
            userRateStatus = animeWithUSerRate.userRate?.status ?: UserRateStatus.None,
            metadata = if (metadata != null) {
                { metadata(animeWithUSerRate) }
            } else null
        )
    }
}
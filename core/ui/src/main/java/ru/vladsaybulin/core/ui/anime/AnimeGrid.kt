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
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeWithUserRate
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun AnimeGrid(
    animes: List<Anime>,
    onEntryClick: (Anime) -> Unit,
    modifier: Modifier = Modifier,
    key: ((Anime) -> Any)? = { it.id },
    columns: GridCells = EntryGridDefaults.DefaultColumns,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = EntryGridDefaults.DefaultContentPadding,
    horizontalArrangement: Arrangement.Horizontal = EntryGridDefaults.DefaultHorizontalArrangement,
    verticalArrangement: Arrangement.Vertical = EntryGridDefaults.DefaultVerticalArrangement,
    metadata: (@Composable ColumnScope.(Anime) -> Unit)? = { AnimeGridMetadata(it) }
) {
    EntryGrid(
        items = animes,
        modifier = modifier,
        key = key,
        columns = columns,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
    ) { anime ->
        AnimeGridItem(
            anime = anime,
            onClick = { onEntryClick(anime) },
            modifier = Modifier.fillMaxWidth(),
            metadata = if (metadata != null) {
                { metadata(anime) }
            } else null
        )
    }
}

@Composable
fun AnimeWithUserRateGrid(
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
fun AnimeWithUserRateStatusGrid(
    items: LazyPagingItems<Anime>,
    onEntryClick: (Anime) -> Unit,
    modifier: Modifier = Modifier,
    key: ((Anime) -> Any)? = { it.id },
    userRateStatus: (Anime) -> UserRateStatus = { UserRateStatus.None },
    columns: GridCells = EntryGridDefaults.DefaultColumns,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = EntryGridDefaults.DefaultContentPadding,
    horizontalArrangement: Arrangement.Horizontal = EntryGridDefaults.DefaultHorizontalArrangement,
    verticalArrangement: Arrangement.Vertical = EntryGridDefaults.DefaultVerticalArrangement,
    metadata: (@Composable ColumnScope.(Anime) -> Unit)? = { AnimeGridMetadata(it) }
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
    ) { anime ->
        AnimeGridItem(
            anime = anime,
            onClick = { onEntryClick(anime) },
            modifier = Modifier.fillMaxWidth(),
            userRateStatus = userRateStatus(anime),
            metadata = if (metadata != null) {
                { metadata(anime) }
            } else null
        )
    }
}
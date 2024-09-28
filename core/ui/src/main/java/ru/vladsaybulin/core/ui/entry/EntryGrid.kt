package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.ErrorMessage

@Composable
fun <T : Any> EntryGrid(
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((T) -> Any)? = null,
    columns: GridCells = EntryGridDefaults.DefaultColumns,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = EntryGridDefaults.DefaultContentPadding,
    horizontalArrangement: Arrangement.Horizontal = EntryGridDefaults.DefaultHorizontalArrangement,
    verticalArrangement: Arrangement.Vertical = EntryGridDefaults.DefaultVerticalArrangement,
    itemContent: @Composable (T) -> Unit,
) {
    LazyVerticalGrid(
        columns = columns,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        modifier = modifier
    ) {
        items(
            items = items,
            key = key,
        ) { item ->
            itemContent(item)
        }
    }
}

@Composable
fun <T : Any> EntryGrid(
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    key: ((T) -> Any)? = null,
    columns: GridCells = EntryGridDefaults.DefaultColumns,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = EntryGridDefaults.DefaultContentPadding,
    horizontalArrangement: Arrangement.Horizontal = EntryGridDefaults.DefaultHorizontalArrangement,
    verticalArrangement: Arrangement.Vertical = EntryGridDefaults.DefaultVerticalArrangement,
    itemContent: @Composable LazyGridItemScope.(T) -> Unit,
) {
    when (val refresh = items.loadState.refresh) {
        is LoadState.Error -> EntryGridRefreshError(throwable = refresh.error)
        else ->
            if (refresh is LoadState.NotLoading && items.itemCount == 0) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                ) {
                    Text(
                        text = "Ничего не найдено",
                        style = SeanimeTheme.typography.titleLarge,
                        color = SeanimeTheme.colorScheme.primary
                    )
                    Text(
                        text = "Попробуйте изменить запрос поиска",
                        style = SeanimeTheme.typography.bodyMedium
                    )
                }
            } else {
                EntryGridContent(
                    items = items,
                    columns = columns,
                    state = state,
                    contentPadding = contentPadding,
                    horizontalArrangement = horizontalArrangement,
                    verticalArrangement = verticalArrangement,
                    key = key,
                    modifier = modifier,
                    itemContent = itemContent
                )
            }
    }
}

@Composable
private fun <T : Any> EntryGridContent(
    items: LazyPagingItems<T>,
    columns: GridCells,
    state: LazyGridState,
    contentPadding: PaddingValues,
    horizontalArrangement: Arrangement.Horizontal,
    verticalArrangement: Arrangement.Vertical,
    key: ((T) -> Any)?,
    modifier: Modifier,
    itemContent: @Composable LazyGridItemScope.(T) -> Unit
) {
    LazyVerticalGrid(
        columns = columns,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        modifier = modifier
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey(key = key)
        ) { index ->
            val item = items[index]
            if (item != null) {
                itemContent(item)
            } else {
                EntryGridItemPlaceholder()
            }
        }
    }
}

@Composable
private fun EntryGridRefreshError(throwable: Throwable) {

    LaunchedEffect(key1 = throwable) {
        throwable.printStackTrace()
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        ErrorMessage(throwable = throwable)
    }
}

@Composable
private fun EntryGridItemPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3 / 4f)
            .background(
                color = SeanimeTheme.colorScheme.outlineVariant,
                shape = EntryGridItemDefaults.shape
            )
            .padding(8.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(24.dp)
                .background(
                    color = SeanimeTheme.colorScheme.outline,
                    shape = EntryGridItemDefaults.shape
                )
        )
    }
}

object EntryGridDefaults {

    val DefaultHorizontalArrangement =
        Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Start)

    val DefaultVerticalArrangement =
        Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Top)

    val DefaultContentPadding = PaddingValues(16.dp)

    val DefaultColumns = GridCells.Adaptive(150.dp)

}
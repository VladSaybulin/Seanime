package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import ru.vladsaybulin.core.ui.ErrorMessage

@Composable
fun <T: Any> EntryGrid(
    items: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    key: ((T) -> Any)? = null,
    columns: GridCells = EntryGridDefaults.DefaultColumns,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = EntryGridDefaults.DefaultContentPadding,
    horizontalArrangement: Arrangement.Horizontal = EntryGridDefaults.DefaultHorizontalArrangement,
    verticalArrangement: Arrangement.Vertical = EntryGridDefaults.DefaultVerticalArrangement,
    itemContent: @Composable (T) -> Unit,
) {
    when (val refresh = items.loadState.refresh) {
        LoadState.Loading -> EntryGridRefreshLoading()
        is LoadState.Error -> EntryGridRefreshError(throwable = refresh.error)
        else -> Unit
    }

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
            }
        }
    }
}

@Composable
private fun EntryGridRefreshLoading() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
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

object EntryGridDefaults {

    val DefaultHorizontalArrangement =
        Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Start)

    val DefaultVerticalArrangement =
        Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Top)

    val DefaultContentPadding = PaddingValues(16.dp)

    val DefaultColumns = GridCells.Adaptive(150.dp)

}
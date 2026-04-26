/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey

@Composable
fun <T : Any> LazyPagingColumn(
    lazyPagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    itemKey: ((T) -> Any)? = null,
    placeholder: (@Composable LazyItemScope.() -> Unit)? = null,
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {

    when (val refresh = lazyPagingItems.loadState.refresh) {
        LoadState.Loading -> LazyPagingRefreshing(modifier)

        is LoadState.Error -> LazyPagingRefreshError(
            refreshError = refresh,
            onRetry = lazyPagingItems::retry,
            modifier = modifier
        )

        is LoadState.NotLoading -> LazyPagingLazyColumnContent(
            lazyPagingItems = lazyPagingItems,
            modifier = modifier,
            state = state,
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            itemKey = itemKey,
            placeholder = placeholder,
            itemContent = itemContent
        )
    }
}

fun <T : Any> LazyListScope.lazyPagingItems(
    lazyPagingItems: LazyPagingItems<T>,
    itemKey: ((T) -> Any)? = null,
    placeholder: (@Composable LazyItemScope.() -> Unit)? = null,
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {
    items(
        count = lazyPagingItems.itemCount,
        key = lazyPagingItems.itemKey(itemKey),
        itemContent = {
            val item = lazyPagingItems[it]
            if (item != null) {
                itemContent(item)
            } else if (placeholder != null) {
                placeholder()
            }
        }
    )

    when (val append = lazyPagingItems.loadState.append) {
        is LoadState.Error -> lazyPagingAppendError(
            appendError = append,
            onRetry = { lazyPagingItems.retry() }
        )

        LoadState.Loading -> lazyPagingAppendLoading()
        is LoadState.NotLoading -> Unit
    }
}

private fun LazyListScope.lazyPagingAppendLoading() {
    item {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            CircularProgressIndicator()
        }
    }
}

private fun LazyListScope.lazyPagingAppendError(
    appendError: LoadState.Error,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    item {
        ErrorMessageRow(
            header = { Text(stringResource(R.string.core_ui_error_message_title)) },
            description = { Text(stringResource(R.string.core_ui_error_message_title)) },
            action = {
                Button(onClick = onRetry) {
                    Text(stringResource(R.string.core_ui_error_retry))
                }
            },
            modifier = modifier
        )
    }
}

@Composable
private fun LazyPagingRefreshError(
    refreshError: LoadState.Error,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    ErrorMessageColumn(
        header = { Text(stringResource(R.string.core_ui_error_message_title)) },
        description = { Text(stringResource(R.string.core_ui_error_message_title)) },
        action = {
            Button(onClick = onRetry) {
                Text(stringResource(R.string.core_ui_error_retry))
            }
        },
        modifier = modifier
    )
}

@Composable
private fun LazyPagingRefreshing(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun <T : Any> LazyPagingLazyColumnContent(
    lazyPagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    itemKey: ((T) -> Any)? = null,
    placeholder: (@Composable LazyItemScope.() -> Unit)? = null,
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {
    LazyColumn(
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        lazyPagingItems(
            lazyPagingItems = lazyPagingItems,
            itemKey = itemKey,
            placeholder = placeholder,
            itemContent = itemContent
        )
    }
}
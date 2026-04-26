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

package ru.vladsaybulin.core.designsystem.components

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarouselDefaults.DefaultArrangement

@Composable
fun <T> ShikimoriCarousel(
    items: List<T>,
    modifier: Modifier = Modifier,
    key: ((item: T) -> Any)? = null,
    contentType: (item: T) -> Any? = { null },
    contentPadding: PaddingValues = ShikimoriCarouselDefaults.contentPadding(),
    listState: LazyListState = rememberLazyListState(),
    arrangement: Arrangement.Horizontal = DefaultArrangement,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {
    ShikimoriCarousel(
        contentPadding = contentPadding,
        arrangement = arrangement,
        modifier = modifier,
        flingBehavior = flingBehavior,
        listState = listState
    ) {
        items(
            items = items,
            itemContent = itemContent,
            key = key,
            contentType = contentType
        )
    }
}

@Composable
fun ShikimoriCarousel(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = ShikimoriCarouselDefaults.contentPadding(),
    arrangement: Arrangement.Horizontal = DefaultArrangement,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        state = listState,
        contentPadding = contentPadding,
        horizontalArrangement = arrangement,
        modifier = modifier,
        content = content,
        flingBehavior = flingBehavior
    )
}

object ShikimoriCarouselDefaults {
    fun contentPadding(
        vertical: Dp = 8.dp,
        horizontal: Dp = 16.dp
    ) = PaddingValues(vertical = vertical, horizontal = horizontal)

    val DefaultArrangement = Arrangement.spacedBy(8.dp)
}



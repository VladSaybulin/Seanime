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

package ru.vladsaybulin.feature.title.details.impl.content

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.ui2.entry.EntryCarousel
import ru.vladsaybulin.core.ui2.entry.title.titleCarouselItems
import ru.vladsaybulin.model.title.Title

@Composable
internal fun SimilarTitle(titles: List<Title>, onTitleClick: (Title) -> Unit) {
    val listState = rememberLazyListState()

    EntryCarousel(
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = listState,
            snapPosition = SnapPosition.Start
        )
    ) {
        titleCarouselItems(
            titles = titles,
            onItemClick = onTitleClick,
            itemModifier = Modifier.width(DefaultSimilarWidth)
        )
    }
}

private val DefaultSimilarWidth = 96.dp
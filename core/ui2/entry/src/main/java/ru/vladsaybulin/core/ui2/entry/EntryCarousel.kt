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

package ru.vladsaybulin.core.ui2.entry

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.core.ui2.entry.anime.animeCarouselItems
import ru.vladsaybulin.core.ui2.entry.preview.ListOfAnimesPreviewParameterProvider

@Composable
fun EntryCarousel(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = EntryCarouselDefaults.contentPadding,
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal = EntryCarouselDefaults.horizontalArrangement(reverseLayout),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    LazyRow(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = content
    )
}

object EntryCarouselDefaults {
    val contentPadding = PaddingValues(
        horizontal = DefaultHorizontalPadding,
        vertical = DefaultVerticalPadding
    )

    fun horizontalArrangement(reverseLayout: Boolean = false) =
        Arrangement.spacedBy(
            space = DefaultArrangeSpace,
            alignment = if (!reverseLayout) Alignment.Start else Alignment.End
        )
}

private val DefaultVerticalPadding = 8.dp
private val DefaultHorizontalPadding = 16.dp

private val DefaultArrangeSpace = 8.dp

@Preview
@Composable
fun EntryCarouselPreview(@PreviewParameter(ListOfAnimesPreviewParameterProvider ::class) animes: List<Anime>) {
    SeanimeTheme {
        Surface {
            EntryCarousel {
                animeCarouselItems(animes, {}, itemModifier = Modifier.width(128.dp))
            }
        }
    }
}
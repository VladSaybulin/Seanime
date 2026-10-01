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

package ru.vladsaybulin.core.ui2.entry.title

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.ui2.entry.EntryItemColorsProducer
import ru.vladsaybulin.model.title.Title
import ru.vladsaybulin.model.userrate.UserRateStatus

inline fun LazyListScope.titleCarouselItems(
    titles: List<Title>,
    crossinline onItemClick: (Title) -> Unit,
    noinline key: ((Title) -> Any)? = { it.id },
    noinline contentType: (item: Title) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    colorsProducer: EntryItemColorsProducer = EntryItemColorsProducer.Surface,
    crossinline userRateStatus: (Title) -> UserRateStatus = { UserRateStatus.None },
    noinline additionalContent: (@Composable (Title) -> Unit)? = null,
) {
    items(
        count = titles.size,
        key = if (key != null) {
            { index -> key(titles[index]) }
        } else null,
        contentType = { index -> contentType(titles[index]) }
    ) { index ->
        val title = titles[index]
        val status = userRateStatus(title)
        TitleCarouselItem(
            title = title,
            onClick = { onItemClick(title) },
            modifier = itemModifier,
            userRateStatus = status,
            colors = colorsProducer(status),
            additionalContent = { additionalContent?.invoke(title) }
        )
    }
}
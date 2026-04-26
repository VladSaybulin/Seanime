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

package ru.vladsaybulin.core.ui2.entry.anime

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.core.ui2.entry.EntryItemColorsProducer

inline fun LazyListScope.animeCarouselItems(
    animes: List<Anime>,
    crossinline onItemClick: (Anime) -> Unit,
    noinline key: ((Anime) -> Any)? = { it.id },
    noinline contentType: (item: Anime) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    colorsProducer: EntryItemColorsProducer = EntryItemColorsProducer.Surface,
    crossinline userRateStatus: (Anime) -> UserRateStatus = { UserRateStatus.None },
    noinline additionalContent: (@Composable (Anime) -> Unit)? = null,
) {
    items(
        count = animes.size,
        key = if (key != null) {
            { index -> key(animes[index]) }
        } else null,
        contentType = { index -> contentType(animes[index]) }
    ) { index ->
        val anime = animes[index]
        val status = userRateStatus(anime)
        AnimeCarouselItem(
            anime = anime,
            onClick = { onItemClick(anime) },
            modifier = itemModifier,
            userRateStatus = status,
            colors = colorsProducer(status),
            additionalContent = { additionalContent?.invoke(anime) }
        )
    }
}
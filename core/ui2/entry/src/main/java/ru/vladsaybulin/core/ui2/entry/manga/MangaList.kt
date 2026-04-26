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

package ru.vladsaybulin.core.ui2.entry.manga

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.core.ui2.entry.EntryItemColorsProducer

inline fun LazyListScope.mangaItems(
    mangas: List<Manga>,
    crossinline onItemClick: (Manga) -> Unit,
    noinline key: ((Manga) -> Any)? = { it.id },
    noinline contentType: (Manga) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    colorsProducer: EntryItemColorsProducer = EntryItemColorsProducer.SurfaceContainer,
    crossinline userRateStatus: (Manga) -> UserRateStatus = { UserRateStatus.None },
    noinline additionalContent: (@Composable (Manga) -> Unit)? = { MangaListItemDefaultAdditionalContent(it) },
) {
    items(
        count = mangas.size,
        key = if (key != null) {
            { index -> key(mangas[index]) }
        } else null,
        contentType = { index -> contentType(mangas[index]) }
    ) { index ->
        val manga = mangas[index]
        val status = userRateStatus(manga)
        MangaListItem(
            manga = manga,
            onClick = { onItemClick(manga) },
            modifier = itemModifier,
            userRateStatus = status,
            colors = colorsProducer(status),
            additionalContent = { additionalContent?.invoke(manga) }
        )
    }
}

inline fun LazyListScope.mangaItems(
    mangas: LazyPagingItems<Manga>,
    crossinline onItemClick: (Manga) -> Unit,
    noinline key: ((Manga) -> Any)? = { it.id },
    noinline contentType: (item: Manga) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    colorsProducer: EntryItemColorsProducer = EntryItemColorsProducer.SurfaceContainer,
    crossinline userRateStatus: (Manga) -> UserRateStatus = { UserRateStatus.None },
    noinline additionalContent: (@Composable (Manga) -> Unit)? = { MangaListItemDefaultAdditionalContent(it) },
) {
    items(
        count = mangas.itemCount,
        key = mangas.itemKey(key),
        contentType = mangas.itemContentType(contentType),
    ) {
        val manga = mangas[it]
        if (manga != null) {
            val status = userRateStatus(manga)
            MangaListItem(
                manga = manga,
                onClick = { onItemClick(manga) },
                modifier = itemModifier,
                userRateStatus = status,
                colors = colorsProducer(status),
                additionalContent = { additionalContent?.invoke(manga) }
            )
        }
    }
}
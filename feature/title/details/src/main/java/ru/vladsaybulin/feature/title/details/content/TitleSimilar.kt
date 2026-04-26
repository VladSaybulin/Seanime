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

package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.core.ui2.entry.EntryCarousel
import ru.vladsaybulin.core.ui2.entry.anime.animeCarouselItems
import ru.vladsaybulin.core.ui2.entry.manga.mangaCarouselItems

@Composable
internal fun TitleSimilarAnimes(animes: List<Anime>, onAnimeClick: (Anime) -> Unit) {
    CommonTitleSimilar {
        animeCarouselItems(
            animes = animes,
            onItemClick = onAnimeClick,
            itemModifier = Modifier.width(DefaultSimilarWidth)
        )
    }
}

@Composable
internal fun TitleSimilarMangas(mangas: List<Manga>, onMangaClick: (Manga) -> Unit) {
    CommonTitleSimilar {
        mangaCarouselItems(
            mangas = mangas,
            onItemClick = onMangaClick,
            itemModifier = Modifier.width(DefaultSimilarWidth)
        )
    }
}

@Composable
private fun CommonTitleSimilar(content: LazyListScope.() -> Unit) {
    val listState = rememberLazyListState()

    EntryCarousel(
        state = listState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = listState,
            snapPosition = SnapPosition.Start
        ),
        content = content
    )
}

private val DefaultSimilarWidth = 96.dp
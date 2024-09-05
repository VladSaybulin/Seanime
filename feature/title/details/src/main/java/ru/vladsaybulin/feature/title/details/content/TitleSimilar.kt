package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.anime.AnimeGridItem
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.core.ui.manga.MangaGridItem
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.manga.Manga

@Composable
internal fun TitleSimilarAnimes(animes: List<Anime>, onAnimeClick: (Anime) -> Unit) {
    CommonTitleSimilar(items = animes) { anime ->
        EntryGridItem(
            name = anime.run { russianName ?: name },
            imageUrl = anime.poster?.previewUrl,
            onClick = { onAnimeClick(anime) },
            nameTextStyle = SeanimeTheme.typography.labelSmall,
            metadata = null,
            modifier = Modifier.width(DefaultSimilarWidth),
        )
    }
}

@Composable
internal fun TitleSimilarMangas(mangas: List<Manga>, onMangaClick: (Manga) -> Unit) {
    CommonTitleSimilar(items = mangas) { manga ->
        EntryGridItem(
            name = manga.run { russianName ?: name },
            imageUrl = manga.poster?.previewUrl,
            onClick = { onMangaClick(manga) },
            metadata = null,
            modifier = Modifier.width(DefaultSimilarWidth),
        )
    }
}

@Composable
private fun <T> CommonTitleSimilar(
    items: List<T>,
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {
    val listState = rememberLazyListState()

    ShikimoriCarousel(
        items = items,
        listState = listState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = listState,
            snapPosition = SnapPosition.Start
        ),
        itemContent = itemContent
    )
}

private val DefaultSimilarWidth = 96.dp
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
import ru.vladsaybulin.ui2.entry.EntryCarousel
import ru.vladsaybulin.ui2.entry.anime.animeCarouselItems
import ru.vladsaybulin.ui2.entry.manga.mangaCarouselItems

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
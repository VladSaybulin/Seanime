package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.ContentWithClickableHeader
import ru.vladsaybulin.core.ui.anime.AnimeCarousel
import ru.vladsaybulin.core.ui.manga.MangaCarousel
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.manga.Manga
import kotlin.math.min

fun LazyListScope.similarAnimeCarousel(
    animes: List<Anime>,
    onShowAllClick: () -> Unit,
    onAnimeClick: (Anime) -> Unit,
) {
    item(key = "anime_similar") {
        val shownAnimes = min(animes.size, MaxShownSimilarEntriesSize)
        val showShowAll = shownAnimes < animes.size

        ContentWithClickableHeader(
            headerText = {
                ShowAllHeaderText(
                    headerText = stringResource(id = R.string.similar),
                    shouldShownShowAll = showShowAll,
                )
            },
            onClick = onShowAllClick,
            enabled = showShowAll
        ) {
            AnimeCarousel(anime = animes, onClick = onAnimeClick)
        }
    }
}

fun LazyListScope.mangaSimilarCarousel(
    mangas: List<Manga>,
    onShowAllClick: () -> Unit,
    onMangaClick: (Manga) -> Unit,
) {
    item(key = "manga_similar") {
        val shownMangas = min(mangas.size, MaxShownSimilarEntriesSize)
        val showShowAll = shownMangas < mangas.size

        ContentWithClickableHeader(
            headerText = {
                ShowAllHeaderText(
                    headerText = stringResource(id = R.string.similar),
                    shouldShownShowAll = showShowAll,
                )
            },
            onClick = onShowAllClick,
            enabled = showShowAll
        ) {
            MangaCarousel(manga = mangas, onClick = onMangaClick)
        }
    }
}

private const val MaxShownSimilarEntriesSize = 10
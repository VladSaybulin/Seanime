package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriModalBottomSheet
import ru.vladsaybulin.core.ui.anime.AnimeGrid
import ru.vladsaybulin.core.ui.manga.MangaGrid
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.manga.Manga

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SimilarAnimeBottomSheet(
    animes: List<Anime>,
    onAnimeClick: (Anime) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ShikimoriModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        val navigationBarsPadding = WindowInsets.navigationBars
            .asPaddingValues().calculateBottomPadding()

        AnimeGrid(
            animes = animes,
            onEntryClick = onAnimeClick,
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp + navigationBarsPadding
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SimilarMangaBottomSheet(
    mangas: List<Manga>,
    onMangaClick: (Manga) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ShikimoriModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier
    ) {
        val navigationBarsPadding = WindowInsets.navigationBars
            .asPaddingValues().calculateBottomPadding()

        MangaGrid(
            items = mangas,
            onEntryClick = onMangaClick,
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp + navigationBarsPadding
            )
        )
    }
}


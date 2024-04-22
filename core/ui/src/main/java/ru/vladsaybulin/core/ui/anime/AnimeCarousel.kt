package ru.vladsaybulin.core.ui.anime

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarouselDefaults
import ru.vladsaybulin.model.anime.Anime

@Composable
fun <T> AnimeCarousel(
    items: List<T>,
    mapAnime: (T) -> Anime,
    onClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ShikimoriCarouselDefaults.contentPadding(),
    metadata: (@Composable (T) -> Unit)? = { AnimeGridMetadata(mapAnime(it)) }
) {
    ShikimoriCarousel(
        items = items,
        modifier = modifier,
        contentPadding = contentPadding,
        key = { mapAnime(it).id }
    ) { entry ->
        val anime = mapAnime(entry)
        AnimeGridItem(
            anime = anime,
            onClick = { onClick(entry) },
            modifier = modifier.width(ItemWidth),
            metadata = if (metadata != null) {
                { metadata(entry) }
            } else null
        )
    }
}

@Composable
fun AnimeCarousel(
    anime: List<Anime>,
    onClick: (Anime) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ShikimoriCarouselDefaults.contentPadding(),
    metadata: (@Composable (Anime) -> Unit)? = { AnimeGridMetadata(it) }
) {
    ShikimoriCarousel(
        items = anime,
        modifier = modifier,
        contentPadding = contentPadding,
        key = { it.id }
    ) { entry ->
        AnimeGridItem(
            anime = entry,
            onClick = { onClick(entry) },
            modifier = modifier.width(ItemWidth),
            metadata = if (metadata != null) {
                { metadata(entry) }
            } else null
        )
    }
}

private val ItemWidth = 148.dp

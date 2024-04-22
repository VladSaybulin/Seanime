package ru.vladsaybulin.core.ui.manga

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarouselDefaults
import ru.vladsaybulin.model.manga.Manga

@Composable
fun MangaCarousel(
    manga: List<Manga>,
    onClick: (Manga) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ShikimoriCarouselDefaults.contentPadding(),
    metadata: (@Composable (Manga) -> Unit)? = { MangaGridMetadata(it) }
) {
    ShikimoriCarousel(
        items = manga,
        modifier = modifier,
        contentPadding = contentPadding,
        key = { it.id }
    ) { entry ->
        MangaGridItem(
            manga = entry,
            onClick = { onClick(entry) },
            modifier = modifier.width(ItemWidth),
            metadata = if (metadata != null) {
                { metadata(entry) }
            } else null
        )
    }
}

private val ItemWidth = 148.dp
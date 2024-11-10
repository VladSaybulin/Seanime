package ru.vladsaybulin.core.ui.entry.carousel.magna

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.ui.entry.grid.manga.MangaGridItem
import ru.vladsaybulin.core.ui.entry.metadata.DefaultMangaGridItemMetadata
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus

fun LazyListScope.mangaCarouselItems(
    items: List<Manga>,
    onClick: (Manga) -> Unit,
    key: ((Manga) -> Any)? = { it.id },
    userRateStatus: (Manga) -> UserRateStatus = { UserRateStatus.None },
    metadata: (@Composable (Manga) -> Unit)? = { DefaultMangaGridItemMetadata(it) }
) {
    items(
        items = items,
        key = key,
    ) { manga ->
        MangaGridItem(
            manga = manga,
            onClick = { onClick(manga) },
            userRateStatus = userRateStatus(manga),
            modifier = Modifier.width(128.dp),
            metadata = if (metadata != null) {
                { metadata(manga) }
            } else null
        )
    }
}
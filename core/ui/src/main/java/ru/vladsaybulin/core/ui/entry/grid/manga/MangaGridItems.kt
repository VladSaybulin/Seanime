package ru.vladsaybulin.core.ui.entry.grid.manga

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import ru.vladsaybulin.core.ui.entry.metadata.MangaMetadataComponents
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus

fun LazyGridScope.mangaGridItems(
    items: LazyPagingItems<Manga>,
    onItemClick: (Manga) -> Unit,
    key: ((Manga) -> Any)? = { "manga_${it.id}" },
    userRateStatus: (Manga) -> UserRateStatus = { UserRateStatus.None },
    metadata: (@Composable (Manga) -> Unit)? = { MangaMetadataComponents.KindAndYearLine(manga = it) }
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key)
    ) {
        val manga = items[it] ?: return@items
        MangaGridItem(
            manga = manga,
            onClick = { onItemClick(manga) },
            modifier = Modifier.fillMaxWidth(),
            userRateStatus = userRateStatus(manga),
            metadata = if (metadata != null) {
                { metadata(manga) }
            } else null
        )
    }
}


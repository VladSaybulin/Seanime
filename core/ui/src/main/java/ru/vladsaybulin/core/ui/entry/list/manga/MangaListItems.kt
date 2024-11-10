package ru.vladsaybulin.core.ui.entry.list.manga

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus

fun LazyListScope.mangaListItems(
    items: List<Manga>,
    onClick: (Manga) -> Unit,
    key: ((Manga) -> Any)? = { it.id },
    userRateStatus: (Manga) -> UserRateStatus,
    metadata: (@Composable (Manga) -> Unit)? = null
) {
    items(
        items = items,
        key = key,
    ) { manga ->
        MangaListItem(
            manga = manga,
            onClick = { onClick(manga) },
            userRateStatus = userRateStatus(manga),
            metadata = metadata?.let { { metadata(manga) } },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun LazyListScope.pagedMangaListItems(
    items: LazyPagingItems<Manga>,
    onClick: (Manga) -> Unit,
    key: ((Manga) -> Any)? = { it.id },
    userRateStatus: (Manga) -> UserRateStatus,
    metadata: (@Composable (Manga) -> Unit)? = null
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key = key),
    ) { index ->
        val manga = items[index] ?: return@items
        MangaListItem(
            manga = manga,
            onClick = { onClick(manga) },
            userRateStatus = userRateStatus(manga),
            metadata = metadata?.let { { metadata(manga) } },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
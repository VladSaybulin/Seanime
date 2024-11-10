package ru.vladsaybulin.core.ui.entry.list.anime

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import ru.vladsaybulin.core.ui.entry.metadata.DefaultAnimeListItemMetadata
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.userrate.UserRateStatus

fun LazyListScope.animeListItems(
    items: List<Anime>,
    onClick: () -> Unit,
    key: ((item: Anime) -> Any)? = null,
    userRateStatus: (anime: Anime) -> UserRateStatus,
    metadata: (@Composable (anime: Anime) -> Unit)? = { DefaultAnimeListItemMetadata(anime = it) }
) {
    items(
        items = items,
        key = key
    ) { anime ->
        AnimeListItem(
            anime = anime,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            userRateStatus = userRateStatus(anime),
            metadata = metadata?.let { { metadata(anime) } }
        )
    }
}

fun LazyListScope.pagedAnimeListItems(
    items: LazyPagingItems<Anime>,
    onClick: () -> Unit,
    key: ((item: Anime) -> Any)? = null,
    userRateStatus: (anime: Anime) -> UserRateStatus,
    metadata: (@Composable (anime: Anime) -> Unit)? = { DefaultAnimeListItemMetadata(anime = it) }
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key = key),
    ) { index ->
        val anime = items[index] ?: return@items
        AnimeListItem(
            anime = anime,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            userRateStatus = userRateStatus(anime),
            metadata = metadata?.let { { metadata(anime) } }
        )
    }
}
package ru.vladsaybulin.core.ui.entry.grid.anime

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import ru.vladsaybulin.core.ui.entry.metadata.AnimeMetadataDefaultComponents
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.userrate.UserRateStatus

fun LazyGridScope.animeGridItems(
    items: LazyPagingItems<Anime>,
    onItemClick: (Anime) -> Unit,
    key: ((Anime) -> Any)? = { "anime_${it.id}" },
    userRateStatus: (Anime) -> UserRateStatus = { UserRateStatus.None },
    metadata: (@Composable ColumnScope.(Anime) -> Unit)? = { AnimeMetadataDefaultComponents.KindAndYear(anime = it) },
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key = key)
    ) { index ->
        val anime = items[index] ?: return@items
        AnimeGridItem(
            anime = anime,
            onClick = { onItemClick(anime) },
            modifier = Modifier.fillMaxWidth(),
            userRateStatus = userRateStatus(anime),
            metadata = if (metadata != null) {
                { metadata(anime) }
            } else null
        )
    }
}

fun LazyListScope.animeCarouselItems(
    items: List<Anime>,
    onEntryClick: (Anime) -> Unit,
    key: ((Anime) -> Any)? = { "anime_${it.id}" },
    userRateStatus: (Anime) -> UserRateStatus = { UserRateStatus.None },
    metadata: (@Composable ColumnScope.(Anime) -> Unit)? = { AnimeMetadataDefaultComponents.KindAndYear(anime = it) },
) {
    items(
        items = items,
        key = key
    ) { anime ->
        AnimeGridItem(
            anime = anime,
            onClick = { onEntryClick(anime) },
            modifier = Modifier.fillMaxWidth(),
            userRateStatus = userRateStatus(anime),
            metadata = if (metadata != null) {
                { metadata(anime) }
            } else null
        )
    }
}
package ru.vladsaybulin.core.ui.entry.carousel.anime

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.ui.entry.grid.anime.AnimeGridItem
import ru.vladsaybulin.core.ui.entry.metadata.DefaultAnimeGridItemMetadata
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.userrate.UserRateStatus

fun LazyListScope.animeCarouselItems(
    items: List<Anime>,
    onItemClick: (Anime) -> Unit,
    itemWidth: Dp = DefaultItemWidth,
    key: ((Anime) -> Any)? = { it.id },
    userRateStatus: (Anime) -> UserRateStatus = { UserRateStatus.None },
    metadata: (@Composable (Anime) -> Unit)? = { DefaultAnimeGridItemMetadata(it) }
) {
    items(
        items = items,
        key = key
    ) { anime ->
        AnimeGridItem(
            anime = anime,
            onClick = { onItemClick(anime) },
            userRateStatus = userRateStatus(anime),
            modifier = Modifier.width(itemWidth),
            metadata = if (metadata != null) {
                { metadata(anime) }
            } else null
        )
    }
}

val DefaultItemWidth = 128.dp
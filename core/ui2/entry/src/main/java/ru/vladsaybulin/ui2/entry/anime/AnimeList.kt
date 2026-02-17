package ru.vladsaybulin.ui2.entry.anime

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.ui2.entry.EntryItemDefaults
import ru.vladsaybulin.ui2.entry.EntryItemStyle

inline fun LazyListScope.animeItems(
    animes: List<Anime>,
    crossinline onItemClick: (Anime) -> Unit,
    noinline key: ((Anime) -> Any)? = { it.id },
    noinline contentType: (item: Anime) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    style: EntryItemStyle? = null,
    crossinline userRateStatus: (Anime) -> UserRateStatus = { UserRateStatus.None },
    noinline additionalContent: (@Composable (Anime) -> Unit)? = { AnimeListItemDefaultAdditionalContent(it) },
) {
    items(
        count = animes.size,
        key = if (key != null) {
            { index -> key(animes[index]) }
        } else null,
        contentType = { index -> contentType(animes[index]) }
    ) { index ->
        val anime = animes[index]
        AnimeListItem(
            anime = anime,
            onClick = { onItemClick(anime) },
            modifier = itemModifier,
            userRateStatus = userRateStatus(anime),
            style = style ?: EntryItemDefaults.regularListStyle(),
            additionalContent = { additionalContent?.invoke(anime) }
        )
    }
}

inline fun LazyListScope.animeItems(
    animes: LazyPagingItems<Anime>,
    crossinline onItemClick: (Anime) -> Unit,
    noinline key: ((Anime) -> Any)? = { it.id },
    noinline contentType: (item: Anime) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    style: EntryItemStyle? = null,
    crossinline userRateStatus: (Anime) -> UserRateStatus = { UserRateStatus.None },
    noinline additionalContent: (@Composable (Anime) -> Unit)? = { AnimeListItemDefaultAdditionalContent(it) },
) {
    items(
        count = animes.itemCount,
        key = animes.itemKey(key),
        contentType = animes.itemContentType(contentType),
    ) {
        val anime = animes[it]
        if (anime != null) {
            AnimeListItem(
                anime = anime,
                onClick = { onItemClick(anime) },
                modifier = itemModifier,
                userRateStatus = userRateStatus(anime),
                style = style ?: EntryItemDefaults.regularGridStyle(),
                additionalContent = { additionalContent?.invoke(anime) }
            )
        }
    }
}

inline fun LazyListScope.animeCarouselItems(
    animes: List<Anime>,
    crossinline onItemClick: (Anime) -> Unit,
    noinline key: ((Anime) -> Any)? = { it.id },
    noinline contentType: (item: Anime) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    style: EntryItemStyle? = null,
    crossinline userRateStatus: (Anime) -> UserRateStatus = { UserRateStatus.None },
    noinline additionalContent: (@Composable (Anime) -> Unit)? = null,
) {
    items(
        count = animes.size,
        key = if (key != null) {
            { index -> key(animes[index]) }
        } else null,
        contentType = { index -> contentType(animes[index]) }
    ) { index ->
        val anime = animes[index]
        AnimeGridItem(
            anime = anime,
            onClick = { onItemClick(anime) },
            modifier = itemModifier,
            userRateStatus = userRateStatus(anime),
            style = style ?: EntryItemDefaults.regularGridStyle(),
            additionalContent = { additionalContent?.invoke(anime) }
        )
    }
}
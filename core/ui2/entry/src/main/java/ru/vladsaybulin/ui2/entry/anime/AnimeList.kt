package ru.vladsaybulin.ui2.entry.anime

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.ui2.entry.EntryItemColorsProducer

inline fun LazyListScope.animeItems(
    animes: List<Anime>,
    crossinline onItemClick: (Anime) -> Unit,
    noinline key: ((Anime) -> Any)? = { it.id },
    noinline contentType: (item: Anime) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    colorsProducer: EntryItemColorsProducer = EntryItemColorsProducer.SurfaceContainer,
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
        val status = userRateStatus(anime)
        AnimeListItem(
            anime = anime,
            onClick = { onItemClick(anime) },
            modifier = itemModifier,
            userRateStatus = status,
            colors = colorsProducer(status),
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
    colorsProducer: EntryItemColorsProducer = EntryItemColorsProducer.BasedOnUserRateStatus,
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
            val status = userRateStatus(anime)
            AnimeListItem(
                anime = anime,
                onClick = { onItemClick(anime) },
                modifier = itemModifier,
                userRateStatus = status,
                colors = colorsProducer(status),
                additionalContent = { additionalContent?.invoke(anime) }
            )
        }
    }
}
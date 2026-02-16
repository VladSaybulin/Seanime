package ru.vladsaybulin.ui2.entry.anime

import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.ui2.entry.EntryItemDefaults
import ru.vladsaybulin.ui2.entry.EntryItemStyle

inline fun LazyGridScope.animeItems(
    animes: List<Anime>,
    crossinline onItemClick: (Anime) -> Unit,
    noinline key: ((Anime) -> Any)? = null,
    noinline contentType: (item: Anime) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    style: EntryItemStyle? = null,
    crossinline userRateStatus: (Anime) -> UserRateStatus = { UserRateStatus.None },
    noinline additionalContent: (@Composable (Anime) -> Unit)? = { AnimeGridItemDefaultAdditionalContent(it) },
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
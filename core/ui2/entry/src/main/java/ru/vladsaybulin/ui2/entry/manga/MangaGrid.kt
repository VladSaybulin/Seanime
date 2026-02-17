package ru.vladsaybulin.ui2.entry.manga

import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.ui2.entry.EntryItemDefaults
import ru.vladsaybulin.ui2.entry.EntryItemStyle

inline fun LazyGridScope.mangaItems(
    mangas: List<Manga>,
    crossinline onItemClick: (Manga) -> Unit,
    noinline key: ((Manga) -> Any)? = { it.id },
    noinline contentType: (Manga) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    style: EntryItemStyle? = null,
    crossinline userRateStatus: (Manga) -> UserRateStatus = { UserRateStatus.None },
    noinline additionalContent: (@Composable (Manga) -> Unit)? = { MangaGridItemDefaultAdditionalContent(it) },
) {
    items(
        count = mangas.size,
        key = if (key != null) {
            { index -> key(mangas[index]) }
        } else null,
        contentType = { index -> contentType(mangas[index]) }
    ) { index ->
        val manga = mangas[index]
        MangaGridItem(
            manga = manga,
            onClick = { onItemClick(manga) },
            modifier = itemModifier,
            userRateStatus = userRateStatus(manga),
            style = style ?: EntryItemDefaults.regularGridStyle(),
            additionalContent = { additionalContent?.invoke(manga) }
        )
    }
}

inline fun LazyGridScope.mangaItems(
    mangas: LazyPagingItems<Manga>,
    crossinline onItemClick: (Manga) -> Unit,
    noinline key: ((Manga) -> Any)? = { it.id },
    noinline contentType: (item: Manga) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    style: EntryItemStyle? = null,
    crossinline userRateStatus: (Manga) -> UserRateStatus = { UserRateStatus.None },
    noinline additionalContent: (@Composable (Manga) -> Unit)? = { MangaGridItemDefaultAdditionalContent(it) },
) {
    items(
        count = mangas.itemCount,
        key = mangas.itemKey(key),
        contentType = mangas.itemContentType(contentType),
    ) {
        val manga = mangas[it]
        if (manga != null) {
            MangaGridItem(
                manga = manga,
                onClick = { onItemClick(manga) },
                modifier = itemModifier,
                userRateStatus = userRateStatus(manga),
                style = style ?: EntryItemDefaults.regularGridStyle(),
                additionalContent = { additionalContent?.invoke(manga) }
            )
        }
    }
}

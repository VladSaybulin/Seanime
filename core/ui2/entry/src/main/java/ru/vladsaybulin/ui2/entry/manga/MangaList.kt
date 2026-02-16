package ru.vladsaybulin.ui2.entry.manga

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.ui2.entry.EntryItemDefaults
import ru.vladsaybulin.ui2.entry.EntryItemStyle

inline fun LazyListScope.mangaItems(
    mangas: List<Manga>,
    crossinline onItemClick: (Manga) -> Unit,
    noinline key: ((Manga) -> Any)? = null,
    noinline contentType: (Manga) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    style: EntryItemStyle? = null,
    crossinline userRateStatus: (Manga) -> UserRateStatus = { UserRateStatus.None },
    noinline additionalContent: (@Composable (Manga) -> Unit)? = { MangaListItemDefaultAdditionalContent(it) },
) {
    items(
        count = mangas.size,
        key = if (key != null) {
            { index -> key(mangas[index]) }
        } else null,
        contentType = { index -> contentType(mangas[index]) }
    ) { index ->
        val manga = mangas[index]
        MangaListItem(
            manga = manga,
            onClick = { onItemClick(manga) },
            modifier = itemModifier,
            userRateStatus = userRateStatus(manga),
            style = style ?: EntryItemDefaults.regularListStyle(),
            additionalContent = { additionalContent?.invoke(manga) }
        )
    }
}

inline fun LazyListScope.mangaCarouselItems(
    mangas: List<Manga>,
    crossinline onItemClick: (Manga) -> Unit,
    noinline key: ((Manga) -> Any)? = null,
    noinline contentType: (Manga) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    style: EntryItemStyle? = null,
    crossinline userRateStatus: (Manga) -> UserRateStatus = { UserRateStatus.None },
    noinline additionalContent: (@Composable (Manga) -> Unit)? = null,
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


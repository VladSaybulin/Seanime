package ru.vladsaybulin.core.ui2.entry.manga

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.core.ui2.entry.EntryItemColorsProducer

inline fun LazyListScope.mangaCarouselItems(
    mangas: List<Manga>,
    crossinline onItemClick: (Manga) -> Unit,
    noinline key: ((Manga) -> Any)? = { it.id },
    noinline contentType: (item: Manga) -> Any? = { null },
    itemModifier: Modifier = Modifier,
    colorsProducer: EntryItemColorsProducer = EntryItemColorsProducer.BasedOnUserRateStatus,
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
        val status = userRateStatus(manga)
        MangaCarouselItem(
            manga = manga,
            onClick = { onItemClick(manga) },
            modifier = itemModifier,
            userRateStatus = status,
            colors = colorsProducer(status),
            additionalContent = { additionalContent?.invoke(manga) }
        )
    }
}

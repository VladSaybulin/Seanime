package ru.vladsaybulin.core.ui.anime

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarouselDefaults
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.UserRateStatus

@Composable
fun AnimeCarousel(
    anime: List<Anime>,
    onClick: (Anime) -> Unit,
    modifier: Modifier = Modifier,
    itemModifier: Modifier = Modifier,
    contentPadding: PaddingValues = ShikimoriCarouselDefaults.contentPadding(),
    details: (@Composable (Anime) -> Unit)? = { AnimeGridMetadata(it) }
) {
    ShikimoriCarousel(
        items = anime,
        modifier = modifier,
        contentPadding = contentPadding,
        key = { it.id }
    ) { entry ->
        EntryGridItem(
            name = entry.russianName ?: entry.name,
            poster = entry.poster,
            onClick = { onClick(entry) },
            userRateStatus = entry.userRate?.status ?: UserRateStatus.None,
            detailsContent = if (details != null) {
                { details(entry) }
            } else null ,
            modifier = itemModifier
        )
    }
}

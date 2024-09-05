package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarouselDefaults
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.model.common.DataSlice
import ru.vladsaybulin.model.common.Image

@Composable
internal fun TitleScreenshots(
    screenshots: List<Image>,
    onScreenshotClick: (index: Int) -> Unit,
) {
    val listState = rememberLazyListState()

    ShikimoriCarousel(
        listState = listState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = listState,
            snapPosition = SnapPosition.Start
        )
    ) {
        itemsIndexed(items = screenshots) { i, image ->
            Screenshot(url = image.originalUrl, onClick = { onScreenshotClick(i) })
        }
    }
}

@Composable
private fun Screenshot(
    url: String,
    onClick: () -> Unit
) {
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(DefaultScreenshotWidth)
            .aspectRatio(DefaultScreenshotAspectRatio)
            .clip(DefaultScreenshotShape)
            .clickable(onClick = onClick)
    )
}

private val DefaultScreenshotShape
    @Composable get() = SeanimeTheme.shapes.large
private val DefaultScreenshotWidth = 200.dp
private const val DefaultScreenshotAspectRatio = 16 / 9f
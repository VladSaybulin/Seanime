package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarouselDefaults
import ru.vladsaybulin.core.designsystem.components.drawForegroundGradientScrim
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.model.anime.Video

@Composable
fun TitleVideos(videos: List<Video>, onVideoClick: (Video) -> Unit) {
    val listState = rememberLazyListState()

    ShikimoriCarousel(
        items = videos,
        listState = listState,
        flingBehavior = rememberSnapFlingBehavior(
            lazyListState = listState,
            snapPosition = SnapPosition.Start
        )
    ) { video ->
        VideoCard(video = video, onClick = { onVideoClick(video) })
    }
}


@Composable
private fun VideoCard(video: Video, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Layout(
        content = {
            video.name?.let {
                VideoName(name = it, modifier = Modifier.layoutId("name"))
            }
            VideoPreviewImage(
                url = video.previewImageUrl,
                modifier = Modifier
                    .layoutId("preview")
                    .drawForegroundGradientScrim(SeanimeTheme.colorScheme.surface)
            )
            VideoPlayIcon(modifier = Modifier.layoutId("icon"))
        },
        modifier = modifier
            .width(VideoCardWidth)
            .aspectRatio(VideoCardAspectRatio)
            .clickable(onClick = onClick)
            .clip(SeanimeTheme.shapes.large),
        measurePolicy = { measurables, constraints ->
            val constraintsForMeasurables = constraints.copy(minWidth = 0, minHeight = 0)

            val namePlaceable =
                measurables.firstOrNull { it.layoutId == "name" }?.measure(constraintsForMeasurables)
            val iconPlaceable = measurables.first { it.layoutId == "icon" }.measure(constraintsForMeasurables)
            val previewPlaceable =
                measurables.first { it.layoutId == "preview" }.measure(constraintsForMeasurables)

            val width = constraints.maxWidth
            val height = constraints.maxHeight

            layout(width, height) {
                previewPlaceable.place(0, 0)
                iconPlaceable.place(
                    (width - iconPlaceable.width) / 2,
                    (height - iconPlaceable.height) / 2
                )
                namePlaceable?.placeRelative(0, height - namePlaceable.height)
            }
        }
    )
}


@Composable
private fun VideoName(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        style = SeanimeTheme.typography.labelSmall,
        color = SeanimeTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.padding(4.dp)
    )
}

@Composable
private fun VideoPreviewImage(
    url: String,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun VideoPlayIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(PlayIconBoxSize)
            .clip(CircleShape)
            .background(SeanimeTheme.colorScheme.surface.copy(alpha = .5f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = SeanimeIcons.PlayArrow,
            contentDescription = null,
            tint = SeanimeTheme.colorScheme.onSurface
        )
    }
}

private val PlayIconBoxSize = 40.dp
private val VideoCardWidth = 200.dp
private const val VideoCardAspectRatio = 16 / 9f
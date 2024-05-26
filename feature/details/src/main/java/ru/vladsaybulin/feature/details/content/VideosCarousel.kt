package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarouselDefaults
import ru.vladsaybulin.core.designsystem.components.drawForegroundGradientScrim
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.ContentWithClickableHeader
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.model.anime.Video
import kotlin.math.min

@Composable
fun VideosCarousel(
    videos: List<Video>,
    onVideoClick: (Video) -> Unit,
    onShowAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shownVideosSize = min(videos.size, MaxShownVideosSize)
    val showShowAll = shownVideosSize < videos.size

    ContentWithClickableHeader(
        headerText = {
            ShowAllHeaderText(
                headerText = stringResource(id = R.string.videos),
                shouldShownShowAll = showShowAll,
            )
        },
        onClick = onShowAllClick,
        modifier = modifier,
        enabled = showShowAll
    ) {
        SeanimeTheme(darkTheme = true) {
            val listState = rememberLazyListState()

            ShikimoriCarousel(
                contentPadding = ShikimoriCarouselDefaults.contentPadding(horizontal = 0.dp, vertical = 0.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(MaterialTheme.shapes.large),
                listState = listState,
                flingBehavior = rememberSnapFlingBehavior(
                    SnapLayoutInfoProvider(
                        lazyListState = listState,
                        snapPosition = SnapPosition.Start,
                    )
                ),
            ) {
                items(count = shownVideosSize) {
                    val video = videos[it]
                    VideoCard(
                        video = video,
                        onClick = { onVideoClick(video) }
                    )
                }

                if (!showShowAll) return@ShikimoriCarousel

                item {
                    ShowMoreVideosCard(
                        previewImageUrl = videos[shownVideosSize].previewImageUrl,
                        onClick = onShowAllClick
                    )
                }
            }
        }
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
private fun ShowMoreVideosCard(
    previewImageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrimColor = SeanimeTheme.colorScheme.surface.copy(alpha = 0.2f)

    Box(
        modifier = modifier
            .width(VideoCardWidth)
            .height((VideoCardWidth / VideoCardAspectRatio))
            .clip(SeanimeTheme.shapes.large)
            .clickable(onClick = onClick)
    ) {
        VideoPreviewImage(
            url = previewImageUrl,
            modifier = Modifier
                .fillMaxSize()
                .blur(16.dp)
                .drawWithContent {
                    drawContent()
                    drawRect(SolidColor(scrimColor))
                }
        )
        Text(
            text = stringResource(id = R.string.more),
            color = SeanimeTheme.colorScheme.onSurface,
            style = SeanimeTheme.typography.labelLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
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
            imageVector = ShikimoriIcons.PlayArrow,
            contentDescription = null,
            tint = SeanimeTheme.colorScheme.onSurface
        )
    }
}

private val PlayIconBoxSize = 40.dp
private val VideoCardWidth = 200.dp
private const val VideoCardAspectRatio = 16 / 9f
private const val MaxShownVideosSize = 5
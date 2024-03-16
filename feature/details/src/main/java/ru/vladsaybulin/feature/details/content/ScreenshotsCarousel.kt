package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarouselDefaults
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.ContentWithClickableHeader
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.model.Screenshot
import kotlin.math.min

@Composable
fun ScreenshotsCarousel(
    screenshots: List<Screenshot>,
    onScreenshotClick: (Screenshot) -> Unit,
    onShowAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shownScreenshotsSize = min(screenshots.size, MaxShownScreenshotsSize)
    val showShowAll = shownScreenshotsSize < screenshots.size

    ContentWithClickableHeader(
        headerText = {
            ShowAllHeaderText(
                headerText = stringResource(id = R.string.screenshots),
                shouldShownShowAll = showShowAll,
            )
        },
        onClick = onShowAllClick,
        modifier = modifier,
        enabled = showShowAll
    ) {
        ShikimoriCarousel(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(ShikimoriTheme.shapes.large),
            contentPadding = ShikimoriCarouselDefaults.contentPadding(horizontal = 0.dp)
        ) {
            items(items = screenshots) { screenshot ->
                ScreenshotCard(
                    screenshot = screenshot,
                    onClick = { onScreenshotClick(screenshot) }
                )
            }

            if (!showShowAll) return@ShikimoriCarousel

            item {
                ShowMoreCard(
                    screenshot = screenshots[shownScreenshotsSize],
                    onClick = onShowAllClick
                )
            }
        }
    }
}

@Composable
private fun ShowMoreCard(
    screenshot: Screenshot,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    ShikimoriTheme(darkTheme = true) {
        val scrimColor = ShikimoriTheme.colorScheme.surface.copy(alpha = 0.5f)

        Box(
            modifier = modifier
                .width(ScreenshotCardWidth)
                .height((ScreenshotCardWidth / ScreenshotCardAspectRatio))
                .clip(ShikimoriTheme.shapes.large)
                .clickable(onClick = onClick)
        ) {
            ScreenshotImage(
                url = screenshot.x166Url,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(32.dp)
                    .drawWithContent {
                        drawContent()
                        drawRect(SolidColor(scrimColor))
                    }

            )
            Text(
                text = stringResource(id = R.string.more),
                color = MaterialTheme.colorScheme.onSurface,
                style = ShikimoriTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun ScreenshotCard(
    screenshot: Screenshot,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(ScreenshotCardWidth)
            .aspectRatio(ScreenshotCardAspectRatio)
            .clip(ShikimoriTheme.shapes.large)
            .clickable(onClick = onClick),
    ) {
        ScreenshotImage(url = screenshot.x332Url)
    }
}

@Composable
private fun ScreenshotImage(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

private const val MaxShownScreenshotsSize = 5
private val ScreenshotCardWidth = 200.dp
private const val ScreenshotCardAspectRatio = 16 / 9f
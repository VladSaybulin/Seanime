package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.ui.colors.onRateStatusColor
import ru.vladsaybulin.core.ui.colors.rateStatusColor
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.model.UserRateStatus

@Composable
internal fun EntryItemName(
    modifier: Modifier = Modifier,
    name: String,
    style: TextStyle = MaterialTheme.typography.titleMedium,
) {
    Text(
        modifier = modifier,
        text = name,
        style = style,
        maxLines = NameTextMaxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
internal fun EntryPoster(
    modifier: Modifier = Modifier,
    poster: Poster?
) {
    if (poster == null) {
        Box(modifier.background(Color.LightGray))
    } else {
        AsyncImage(
            modifier = modifier,
            model = poster.previewUrl,
            contentDescription = null,
        )
    }
}

@Composable
internal fun UserRateStatusIcon(
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus
) {
    val userRateStatusColor = rateStatusColor(userRateStatus)
    val userRateIconColor = onRateStatusColor(userRateStatus)

    val iconPainter = userRateStatusPainter(userRateStatus = userRateStatus) ?: return

    Canvas(modifier = modifier.aspectRatio(1f)) {
        drawPath(
            path = Path().apply {
                moveTo(0.0f, size.height)
                lineTo(size.width, 0.0f)
                lineTo(size.width, size.height)
                close()
            },
            brush = SolidColor(userRateStatusColor)
        )

        val size = size.width

        val halfImageSizeFraction = 0.25f

        inset(
            top = size * (0.70f - halfImageSizeFraction),
            left = size * (0.70f - halfImageSizeFraction),
            bottom = size - size * (0.70f + halfImageSizeFraction),
            right = size - size * (0.70f + halfImageSizeFraction)
        ) {
            with(iconPainter) {
                draw(
                    size = this@inset.size,
                    colorFilter = ColorFilter.tint(userRateIconColor)
                )
            }
        }
    }
}

@Composable
private fun userRateStatusPainter(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> ShikimoriIcons.Add
    UserRateStatus.Watching -> ShikimoriIcons.Visibility
    UserRateStatus.Rewatching -> ShikimoriIcons.Replay
    UserRateStatus.Completed -> ShikimoriIcons.Done
    UserRateStatus.Dropped -> ShikimoriIcons.Clear
    UserRateStatus.OnHold -> ShikimoriIcons.Pause
    else -> null
}
    ?.let { rememberVectorPainter(it) }

private const val NameTextMaxLines = 2
package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.colors.onUserRateStatusColor
import ru.vladsaybulin.core.ui.colors.userRateStatusColor
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
        Box(modifier.background(Color.White))
    } else {
        AsyncImage(
            modifier = modifier,
            model = poster.originalUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
internal fun UserRateStatusBadge(
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus
) {
    val userRateStatusColor = userRateStatusColor(userRateStatus)
    val userRateIconColor = onUserRateStatusColor(userRateStatus)

    val icon = userRateStatusIcon(userRateStatus = userRateStatus) ?: return

    Box(
        modifier = modifier
            .clip(DefaultShape)
            .background(userRateStatusColor)
            .padding(DefaultIconPadding),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = userRateIconColor,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
@ReadOnlyComposable
private fun userRateStatusIcon(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> ShikimoriIcons.Add
    UserRateStatus.Watching -> ShikimoriIcons.Visibility
    UserRateStatus.Rewatching -> ShikimoriIcons.Replay
    UserRateStatus.Completed -> ShikimoriIcons.Done
    UserRateStatus.Dropped -> ShikimoriIcons.Clear
    UserRateStatus.OnHold -> ShikimoriIcons.Pause
    else -> null
}

private const val NameTextMaxLines = 2
private val DefaultShape
    @Composable get() = ShikimoriTheme.shapes.medium.copy(
        topEnd = ZeroCornerSize,
        bottomEnd = ZeroCornerSize,
        topStart = ZeroCornerSize
    )
private val DefaultIconPadding = PaddingValues(4.dp)
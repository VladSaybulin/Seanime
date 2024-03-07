package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.drawForegroundGradientScrim
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.model.UserRateStatus

@Composable
internal fun EntryGridItem(
    name: String,
    poster: Poster?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    nameTextStyle: TextStyle = MaterialTheme.typography.labelLarge,
    bodyTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    detailsContent: (@Composable () -> Unit)? = null,
) {
    ShikimoriTheme(darkTheme = true) {
        Box(
            modifier = modifier
                .aspectRatio(EntryGridItemAspectRatio)
                .clip(ShikimoriTheme.shapes.medium)
                .clickable(onClick = onClick)
        ) {
            val scrimColor = ShikimoriTheme.colorScheme.surface.copy(alpha = GradientScrimAlpha)
            val contentColor = ShikimoriTheme.colorScheme.onSurface

            EntryPoster(
                poster = poster,
                modifier = Modifier
                    .fillMaxSize()
                    .drawForegroundGradientScrim(scrimColor, decay = 3f)
            )

            UserRateStatusBadge(
                userRateStatus = userRateStatus,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(4.dp)
                    .padding(top = 16.dp)
            ) {
                CompositionLocalProvider(value = LocalContentColor provides contentColor) {
                    EntryItemName(name = name, style = nameTextStyle)
                    if (detailsContent != null) {
                        ProvideTextStyle(value = bodyTextStyle) {
                            detailsContent()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun SmallEntryGridItem(
    modifier: Modifier,
    name: String,
    poster: Poster?,
    onClick: () -> Unit,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    nameTextStyle: TextStyle = MaterialTheme.typography.labelMedium,
    bodyTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    detailsContent: (@Composable () -> Unit)? = null,
) {
    EntryGridItem(
        modifier = modifier,
        name = name,
        poster = poster,
        onClick = onClick,
        userRateStatus = userRateStatus,
        nameTextStyle = nameTextStyle,
        bodyTextStyle = bodyTextStyle,
        detailsContent = detailsContent
    )
}


@Composable
@Preview
fun EntryGridItemPreview() {
    ShikimoriTheme {
        EntryGridItem(
            name = "Entry name",
            poster = null,
            onClick = { },
            modifier = Modifier.width(150.dp)
        )
    }
}

@Composable
@Preview
fun EntryGridItemWithLongNamePreview() {
    ShikimoriTheme {
        EntryGridItem(
            name = "This is a long entry name that does not fit into 2 lines",
            poster = null,
            onClick = { },
            modifier = Modifier.width(150.dp)
        )
    }
}

@Composable
@Preview
fun EntryGridItemWithDetailsPreview() {
    ShikimoriTheme {
        EntryGridItem(
            name = "Entry name",
            poster = null,
            onClick = { },
            modifier = Modifier.width(150.dp),
            detailsContent = {
                Text(text = "Details content")
            }
        )
    }
}

@Composable
@Preview
fun EntryGridItemWithUserRateStatusPreview() {
    ShikimoriTheme {
        EntryGridItem(
            name = "Entry name",
            poster = null,
            onClick = { },
            modifier = Modifier.width(150.dp),
            userRateStatus = UserRateStatus.Planned,
            detailsContent = {
                Text(text = "Details content")
            }
        )
    }
}

private const val GradientScrimAlpha = 0.8f
private val UserRateStatusIconSize = 32.dp
private const val EntryGridItemAspectRatio = 3 / 4f
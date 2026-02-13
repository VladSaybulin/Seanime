package ru.vladsaybulin.ui2.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.userRateStatusIcon
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.designsystem.theme.get
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun EntryGridItem(
    name: String,
    russianName: String?,
    poster: Image?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    style: EntryItemStyle = EntryItemDefaults.regularGridStyle(),
    additionalContent: (@Composable () -> Unit)? = null,
) {
    EntryItem(
        modifier = modifier,
        posterModifier = Modifier,
        name = name,
        russianName = russianName,
        poster = poster,
        style = style,
        onClick = onClick,
        userRateStatus = userRateStatus,
        additionalContent = additionalContent,
        horizontal = false
    )
}

@Composable
fun EntryListItem(
    name: String,
    russianName: String?,
    poster: Image?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    posterWidth: Dp = EntryItemDefaults.listItemPosterWidth,
    style: EntryItemStyle = EntryItemDefaults.regularListStyle(),
    additionalContent: (@Composable () -> Unit)? = null,
) {
    EntryItem(
        modifier = modifier,
        posterModifier = Modifier.width(posterWidth),
        name = name,
        russianName = russianName,
        poster = poster,
        style = style,
        onClick = onClick,
        userRateStatus = userRateStatus,
        additionalContent = additionalContent,
        horizontal = true
    )
}

data class EntryItemStyle(
    val nameStyle: TextStyle,
    val nameMaxLines: Int,
    val additionalContentStyle: TextStyle,
    val infoPadding: PaddingValues,
    val badgeSize: Dp,
    val shape: Shape,
    val surfaceColorByStatus: Boolean
)

object EntryItemDefaults {

    val listItemPosterWidth = 96.dp

    @Composable
    fun regularGridStyle(
        nameStyle: TextStyle = SeanimeTheme.typography.titleSmall,
        nameMaxLines: Int = 2,
        additionalContentStyle: TextStyle = SeanimeTheme.typography.labelSmall,
        infoPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        badgeSize: Dp = 32.dp,
        shape: Shape = RoundedCornerShape(16.dp),
        surfaceColorByStatus: Boolean = true
    ): EntryItemStyle = EntryItemStyle(
        nameStyle = nameStyle,
        nameMaxLines = nameMaxLines,
        additionalContentStyle = additionalContentStyle,
        infoPadding = infoPadding,
        badgeSize = badgeSize,
        shape = shape,
        surfaceColorByStatus = surfaceColorByStatus
    )

    @Composable
    fun regularListStyle(
        nameStyle: TextStyle = SeanimeTheme.typography.titleSmall,
        nameMaxLines: Int = 2,
        additionalContentStyle: TextStyle = SeanimeTheme.typography.labelSmall,
        infoPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        badgeSize: Dp = 32.dp,
        shape: Shape = RoundedCornerShape(16.dp),
        surfaceColorByStatus: Boolean = true
    ): EntryItemStyle = EntryItemStyle(
        nameStyle = nameStyle,
        nameMaxLines = nameMaxLines,
        additionalContentStyle = additionalContentStyle,
        infoPadding = infoPadding,
        badgeSize = badgeSize,
        shape = shape,
        surfaceColorByStatus = surfaceColorByStatus
    )
}

@Composable
internal fun EntryItem(
    modifier: Modifier,
    posterModifier: Modifier,
    name: String,
    russianName: String?,
    poster: Image?,
    style: EntryItemStyle,
    onClick: () -> Unit,
    userRateStatus: UserRateStatus,
    additionalContent: (@Composable () -> Unit)?,
    horizontal: Boolean = false,
) {
    val posterContent = @Composable { m: Modifier ->
        EntryItemPosterWithStatusBadge(
            poster = poster,
            userRateStatus = userRateStatus,
            containerShape = style.shape,
            badgeSize = style.badgeSize,
            modifier = m
        )
    }

    val infoContent = @Composable { m: Modifier ->
        Column(modifier = m.padding(style.infoPadding)) {
            EntryItemName(
                name = name,
                russianName = russianName,
                style = style.nameStyle,
                maxLines = style.nameMaxLines
            )

            if (additionalContent != null) {
                ProvideTextStyle(
                    value = style.additionalContentStyle,
                    content = additionalContent
                )
            }
        }
    }

    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        color = style.containerColor(userRateStatus),
        contentColor = style.contentColor(userRateStatus),
        shape = style.shape
    ) {
        if (horizontal) {
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically
            ) {
                posterContent(posterModifier.fillMaxHeight())
                infoContent(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        } else {
            Column {
                posterContent(posterModifier)
                infoContent(Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun EntryItemName(
    name: String,
    russianName: String?,
    style: TextStyle,
    maxLines: Int = 2
) {
    val finalName = if (!russianName.isNullOrBlank() && isRussianName()) russianName else name

    Text(
        text = finalName,
        style = style,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun EntryItemPosterWithStatusBadge(
    poster: Image?,
    userRateStatus: UserRateStatus,
    containerShape: Shape,
    badgeSize: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(containerShape)
            .background(SeanimeTheme.colorScheme.surfaceContainerHigh)
    ) {
        val badgeShape = if (containerShape is CornerBasedShape) {
            containerShape.copy(
                topStart = ZeroCornerSize,
                bottomEnd = ZeroCornerSize,
            )
        } else containerShape

        EntryPoster(
            posterUrl = poster?.previewUrl,
            modifier = Modifier.fillMaxWidth()
        )

        if (userRateStatus != UserRateStatus.None) {
            EntryItemBadge(
                userRateStatus = userRateStatus,
                modifier = Modifier
                    .clip(badgeShape)
                    .size(badgeSize)
                    .align(Alignment.TopEnd),
            )
        }
    }
}

@Composable
private fun EntryItemBadge(
    userRateStatus: UserRateStatus,
    modifier: Modifier = Modifier,
) {
    val colors = SeanimeTheme.seanimeColors[userRateStatus]

    Box(
        modifier = modifier
            .sizeIn(minWidth = 24.dp, minHeight = 24.dp)
            .background(colors.color)
    ) {
        Box(modifier = Modifier.matchParentSize(), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = userRateStatusIcon(userRateStatus),
                contentDescription = null,
                tint = colors.onColor,
                modifier = Modifier.fillMaxSize(0.75f)
            )
        }
    }
}

@Composable
@ReadOnlyComposable
private fun isRussianName(): Boolean {
    val locale = LocalConfiguration.current.locales.get(0)
    val isRussian = locale?.language == "ru"
    return isRussian
}

@Composable
fun EntryItemStyle.containerColor(userRateStatus: UserRateStatus): Color {
    return if (surfaceColorByStatus && userRateStatus != UserRateStatus.None) {
        SeanimeTheme.seanimeColors[userRateStatus].container
    } else {
        SeanimeTheme.colorScheme.surfaceContainer
    }
}

@Composable
fun EntryItemStyle.contentColor(userRateStatus: UserRateStatus): Color {
    return if (surfaceColorByStatus && userRateStatus != UserRateStatus.None) {
        SeanimeTheme.seanimeColors[userRateStatus].onContainer
    } else {
        SeanimeTheme.colorScheme.onSurface
    }
}

class EntryItemBadgePreviewParameterProvider : PreviewParameterProvider<UserRateStatus> {
    override val values: Sequence<UserRateStatus> = UserRateStatus.entries
        .filter { it != UserRateStatus.None }
        .asSequence()
}

@Preview
@Composable
fun EntryItemBadgePreview(
    @PreviewParameter(EntryItemBadgePreviewParameterProvider::class) userRateStatus: UserRateStatus
) {
    SeanimeTheme {
        EntryItemBadge(
            userRateStatus = userRateStatus,
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 16.dp))
                .size(48.dp)
        )
    }
}

@Composable
@Preview
fun EntryListItemWithMaxHeightAdditionalContentPreview() {
    SeanimeTheme {
        EntryListItem(
            name = "EN name",
            russianName = "RU name",
            poster = null,
            onClick = {},
            modifier = Modifier,
            additionalContent = {
                Box(modifier = Modifier.width(32.dp).fillMaxHeight().background(Color.Magenta))
            }
        )
    }
}

@Composable
@Preview
fun EntryListItemWithOverflowByHeightAdditionalContentPreview() {
    SeanimeTheme {
        EntryListItem(
            name = "EN name",
            russianName = "RU name",
            poster = null,
            onClick = {},
            modifier = Modifier,
            additionalContent = {
                Box(modifier = Modifier.width(32.dp).height(320.dp).background(Color.Magenta))
            }
        )
    }
}
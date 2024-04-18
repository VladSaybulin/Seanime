package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.EntryPoster
import ru.vladsaybulin.core.ui.colors.onUserRateStatusContainerColor
import ru.vladsaybulin.core.ui.colors.userRateStatusContainerColor
import ru.vladsaybulin.model.common.Poster
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun EntryRow(
    name: String,
    poster: Poster?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    colors: EntryRowColors = EntryRowDefaults.colorsFor(userRateStatus),
    spacing: EntryRowSpacing = EntryRowDefaults.spacing(),
    posterWidth: Dp = EntryRowDefaults.NormalPosterWidth,
    nameTextStyle: TextStyle = EntryRowDefaults.normalNameTextStyle,
    bodyTextStyle: TextStyle = EntryRowDefaults.normalBodyTextStyle,
    border: BorderStroke? = null,
    shape: Shape = ShikimoriTheme.shapes.medium,
    posterShape: Shape = ShikimoriTheme.shapes.medium,
    details: (@Composable () -> Unit)? = null
) {
    Surface(
        onClick = onClick,
        color = colors.containerColor,
        contentColor = colors.contentColor,
        border = border,
        shape = shape,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(spacing.contentPadding)
                .height(IntrinsicSize.Min)
        ) {
            Box(modifier = Modifier.clip(posterShape)) {
                EntryPoster(
                    poster = poster,
                    modifier = Modifier.width(posterWidth)
                )
                if (userRateStatus != UserRateStatus.None) {
                    UserRateStatusBadge(
                        userRateStatus = userRateStatus,
                        modifier = Modifier
                            .size(EntryRowDefaults.UserRateStatusBadgeSize)
                            .align(Alignment.TopEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.width(spacing.posterSpace))

            Column(modifier = Modifier.fillMaxHeight().padding(8.dp)) {
                EntryRowName(
                    name = name,
                    textStyle = nameTextStyle
                )
                if (details != null) {
                    Spacer(modifier = Modifier.height(spacing.detailsTopSpace))
                    Box(
                        modifier = Modifier
                            .clipToBounds()
                            .fillMaxSize()
                    ) {
                        ProvideTextStyle(value = bodyTextStyle, content = details)
                    }
                }
            }
        }
    }
}

@Composable
private fun EntryRowName(
    name: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Text(
        text = name,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        style = textStyle,
        modifier = modifier
    )
}

data class EntryRowSpacing(
    val contentPadding: PaddingValues,
    val posterSpace: Dp,
    val detailsTopSpace: Dp
)

data class EntryRowColors(
    val containerColor: Color,
    val contentColor: Color
)

object EntryRowDefaults {

    @Composable
    fun colorsFor(userRateStatus: UserRateStatus) = EntryRowColors(
        containerColor = userRateStatusContainerColor(userRateStatus = userRateStatus),
        contentColor = onUserRateStatusContainerColor(userRateStatus = userRateStatus)
    )

    @Composable
    fun colors(
        containerColor: Color = ShikimoriTheme.colorScheme.surface,
        contentColor: Color = ShikimoriTheme.colorScheme.onSurface
    ) = EntryRowColors(
        containerColor = containerColor,
        contentColor = contentColor
    )

    @Composable
    fun spacing(
        contentPadding: PaddingValues = NormalContentPadding,
        posterSpace: Dp = NormalPosterSpace,
        detailsTopSpace: Dp = NormalDetailsTopSpace
    ) = EntryRowSpacing(
        contentPadding = contentPadding,
        posterSpace = posterSpace,
        detailsTopSpace = detailsTopSpace
    )

    @Composable
    fun smallSpacing(
        contentPadding: PaddingValues = SmallContentPadding,
        posterWidth: Dp = SmallPosterWidth,
        posterSpace: Dp = SmallPosterSpace,
        detailsTopSpace: Dp = SmallDetailsTopSpace
    ) = EntryRowSpacing(
        contentPadding = contentPadding,
        posterSpace = posterSpace,
        detailsTopSpace = detailsTopSpace
    )

    @Composable
    fun largeSpacing(
        contentPadding: PaddingValues = NormalContentPadding,
        posterWidth: Dp = LargePosterWidth,
        posterSpace: Dp = NormalPosterSpace,
        detailsTopSpace: Dp = NormalDetailsTopSpace
    ) = EntryRowSpacing(
        contentPadding = contentPadding,
        posterSpace = posterSpace,
        detailsTopSpace = detailsTopSpace
    )

    val SmallContentPadding = PaddingValues(4.dp)
    val NormalContentPadding = PaddingValues(8.dp)

    val SmallPosterWidth = 72.dp
    val NormalPosterWidth = 96.dp
    val LargePosterWidth = 128.dp

    val SmallPosterSpace = 4.dp
    val NormalPosterSpace = 8.dp

    val SmallDetailsTopSpace = 2.dp
    val NormalDetailsTopSpace = 4.dp

    val UserRateStatusBadgeSize = 24.dp

    val SmallNameTextStyle
        @Composable get() = ShikimoriTheme.typography.titleSmall

    val normalNameTextStyle
        @Composable get() = ShikimoriTheme.typography.titleMedium

    val SmallBodyTextStyle
        @Composable get() = ShikimoriTheme.typography.bodySmall

    val normalBodyTextStyle
        @Composable get() = ShikimoriTheme.typography.bodyMedium
}

@Preview
@Composable
fun EntryRowPreview() {
    ShikimoriTheme {
        EntryRow(
            name = "Entry name",
            poster = Poster(""),
            onClick = {},
            userRateStatus = UserRateStatus.Planned
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Red))
        }
    }
}
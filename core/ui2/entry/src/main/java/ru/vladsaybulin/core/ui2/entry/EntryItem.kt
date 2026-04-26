/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.core.ui2.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
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
@NonRestartableComposable
fun EntryGridItem(
    name: String,
    russianName: String?,
    poster: Image?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    colors: EntryItemColors = EntryItemDefaults.basedOnUserRateStatusColors(userRateStatus),
    nameStyle: TextStyle = EntryItemDefaults.GridNameStyle,
    infoPadding: PaddingValues = EntryItemDefaults.GridPadding,
    badgeSize: Dp = EntryItemDefaults.GridBadgeSize,
    shape: Shape = EntryItemDefaults.GridShape,
    additionalContent: (@Composable () -> Unit)? = null,
) {
    EntryItem(
        modifier = modifier,
        posterModifier = Modifier,
        name = name,
        russianName = russianName,
        poster = poster,
        userRateStatus = userRateStatus,
        onClick = onClick,
        horizontal = false,
        colors = colors,
        nameStyle = nameStyle,
        nameMaxLines = ENTRY_GRID_ITEM_NAME_MAX_LINES,
        infoPadding = infoPadding,
        badgeSize = badgeSize,
        shape = shape,
        additionalContent = additionalContent
    )
}

@Composable
@NonRestartableComposable
fun EntryListItem(
    name: String,
    russianName: String?,
    poster: Image?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    colors: EntryItemColors = EntryItemDefaults.SurfaceContainerColors,
    posterWidth: Dp = EntryItemDefaults.ListItemPosterWidth,
    nameStyle: TextStyle = EntryItemDefaults.ListNameStyle,
    infoPadding: PaddingValues = EntryItemDefaults.ListPadding,
    badgeSize: Dp = EntryItemDefaults.ListBadgeSize,
    shape: Shape = EntryItemDefaults.ListShape,
    additionalContent: (@Composable () -> Unit)? = null,
) {
    EntryItem(
        modifier = modifier,
        posterModifier = Modifier.width(posterWidth),
        name = name,
        russianName = russianName,
        poster = poster,
        userRateStatus = userRateStatus,
        onClick = onClick,
        horizontal = true,
        colors = colors,
        nameStyle = nameStyle,
        nameMaxLines = ENTRY_LIST_ITEM_NAME_MAX_LINES,
        infoPadding = infoPadding,
        badgeSize = badgeSize,
        shape = shape,
        additionalContent = additionalContent
    )
}

@Composable
@NonRestartableComposable
fun EntryCarouselItem(
    name: String,
    russianName: String?,
    poster: Image?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    colors: EntryItemColors = EntryItemDefaults.SurfaceColors,
    nameStyle: TextStyle = EntryItemDefaults.CarouselNameStyle,
    infoPadding: PaddingValues = EntryItemDefaults.CarouselPadding,
    badgeSize: Dp = EntryItemDefaults.CarouselBadgeSize,
    shape: Shape = EntryItemDefaults.CarouselShape,
    additionalContent: (@Composable () -> Unit)? = null,
) {
    EntryItem(
        modifier = modifier,
        posterModifier = Modifier,
        name = name,
        russianName = russianName,
        poster = poster,
        userRateStatus = userRateStatus,
        onClick = onClick,
        horizontal = false,
        colors = colors,
        nameStyle = nameStyle,
        nameMaxLines = ENTRY_CAROUSEL_ITEM_NAME_MAX_LINES,
        infoPadding = infoPadding,
        badgeSize = badgeSize,
        shape = shape,
        additionalContent = additionalContent
    )
}

data class EntryItemColors(
    val containerColor: Color,
    val contentColor: Color
)

object EntryItemDefaults {

    val SurfaceColors: EntryItemColors
        @Composable @ReadOnlyComposable get() = EntryItemColors(
            containerColor = SeanimeTheme.colorScheme.surface,
            contentColor = SeanimeTheme.colorScheme.onSurface
        )

    val SurfaceContainerColors: EntryItemColors
        @Composable @ReadOnlyComposable get() = EntryItemColors(
            containerColor = SeanimeTheme.colorScheme.surfaceContainer,
            contentColor = SeanimeTheme.colorScheme.onSurface
        )

    @Composable
    @ReadOnlyComposable
    fun basedOnUserRateStatusColors(userRateStatus: UserRateStatus): EntryItemColors {
            val colors = if (userRateStatus != UserRateStatus.None) {
                SeanimeTheme.seanimeColors[userRateStatus]
            } else {
                return SurfaceContainerColors
            }
            return EntryItemColors(
                containerColor = colors.container,
                contentColor = colors.onContainer
            )
        }

    val GridNameStyle: TextStyle
        @Composable @ReadOnlyComposable get() = SeanimeTheme.typography.labelSmall

    val GridPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp)

    val GridBadgeSize: Dp = 32.dp

    val GridShape: Shape = RoundedCornerShape(16.dp)


    val ListNameStyle: TextStyle
        @Composable @ReadOnlyComposable get() = SeanimeTheme.typography.titleSmall

    val ListPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 2.dp)

    val ListBadgeSize: Dp = 28.dp

    val ListShape: Shape = RoundedCornerShape(16.dp)

    val ListItemPosterWidth = 100.dp

    val CarouselNameStyle: TextStyle
        @Composable @ReadOnlyComposable get() = SeanimeTheme.typography.labelSmall

    val CarouselPadding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 4.dp)

    val CarouselBadgeSize: Dp = 24.dp

    val CarouselShape: Shape = RoundedCornerShape(8.dp)
}

@Composable
internal fun EntryItem(
    modifier: Modifier,
    posterModifier: Modifier,
    name: String,
    russianName: String?,
    poster: Image?,
    userRateStatus: UserRateStatus,
    onClick: () -> Unit,
    horizontal: Boolean = false,
    colors: EntryItemColors,
    nameStyle: TextStyle,
    nameMaxLines: Int,
    infoPadding: PaddingValues,
    badgeSize: Dp,
    shape: Shape,
    additionalContent: (@Composable () -> Unit)?,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        color = colors.containerColor,
        contentColor = colors.contentColor,
        shape = shape
    ) {
        EntryItemLayout(
            poster = @Composable { m: Modifier ->
                EntryItemPosterWithStatusBadge(
                    poster = poster,
                    userRateStatus = userRateStatus,
                    containerShape = shape,
                    badgeSize = badgeSize,
                    modifier = posterModifier.then(m)
                )
            },
            info = @Composable { m: Modifier ->
                Column(modifier = m.padding(infoPadding)) {
                    EntryItemName(
                        name = name,
                        russianName = russianName,
                        style = nameStyle,
                        maxLines = nameMaxLines
                    )

                    additionalContent?.invoke()
                }
            },
            horizontal = horizontal
        )
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

private const val ENTRY_GRID_ITEM_NAME_MAX_LINES = 1
private const val ENTRY_LIST_ITEM_NAME_MAX_LINES = 2
private const val ENTRY_CAROUSEL_ITEM_NAME_MAX_LINES = 1

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
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .fillMaxHeight()
                        .background(Color.Magenta)
                )
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
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(320.dp)
                        .background(Color.Magenta)
                )
            }
        )
    }
}
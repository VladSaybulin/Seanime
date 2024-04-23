package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.UserRateStatusBadge
import ru.vladsaybulin.core.ui.UserRateStatusBadgeDefaults
import ru.vladsaybulin.model.userrate.UserRateStatus
import kotlin.math.roundToInt

@Composable
fun EntryListItem(
    name: String,
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    contentPadding: PaddingValues = EntryListDefaults.ContentPadding,
    containerColor: Color = ShikimoriTheme.colorScheme.surface,
    contentColor: Color = ShikimoriTheme.colorScheme.onSurface,
    border: BorderStroke? = null,
    containerShape: Shape = ShikimoriTheme.shapes.large,
    imageIgnoresPadding: Boolean = false,
    imageWidth: Dp = EntryListDefaults.ImageWidth,
    imageShape: CornerBasedShape = ShikimoriTheme.shapes.medium,
    metadata: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        border = border,
        shape = containerShape,
        color = containerColor,
        contentColor = contentColor
    ) {
        Layout(
            measurePolicy = EntryListItemMeasurePolicy(
                contentPadding,
                imageIgnoresPadding,
                imageWidth
            ),
            modifier = modifier.fillMaxWidth(),
            content = {
                EntryListItemName(name = name)
                EntryListItemImage(
                    imageUrl = imageUrl,
                    shape = imageShape,
                )
                UserRateStatusBadge(
                    userRateStatus = userRateStatus,
                    shape = UserRateStatusBadgeDefaults.topEndShape(imageShape),
                    modifier = Modifier
                        .size(UserRateStatusBadgeSize)
                        .layoutId(EntryListItemLayoutId.UserRateStatus)
                )
                if (metadata != null) {
                    ProvideTextStyle(value = ShikimoriTheme.typography.bodySmall) {
                        Column(
                            modifier = Modifier.layoutId(EntryListItemLayoutId.Metadata),
                            content = metadata
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun EntryListItemName(name: String) {
    Text(
        text = name,
        style = ShikimoriTheme.typography.titleMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.layoutId(EntryListItemLayoutId.Name)
    )
}

@Composable
private fun EntryListItemImage(
    imageUrl: String?,
    shape: Shape
) {

    var isError by remember { mutableStateOf(false) }

    val painter = if (imageUrl == null || isError || LocalInspectionMode.current) {
        painterResource(id = R.drawable.no_poster)
    } else {
        rememberAsyncImagePainter(
            model = imageUrl,
            onState = { isError = it is AsyncImagePainter.State.Error }
        )
    }

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = Crop,
        modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .layoutId(EntryListItemLayoutId.Image)
    )
}

private enum class EntryListItemLayoutId {
    Image, Name, Metadata, UserRateStatus
}

class EntryListItemMeasurePolicy(
    private val contentPadding: PaddingValues,
    private val imageIgnoresPadding: Boolean,
    private val imageWidthDp: Dp
) : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {

        val startPadding = contentPadding.calculateStartPadding(layoutDirection).roundToPx()
        val topPadding = contentPadding.calculateTopPadding().roundToPx()
        val endPadding = contentPadding.calculateEndPadding(layoutDirection).roundToPx()
        val bottomPadding = contentPadding.calculateBottomPadding().roundToPx()

        val verticalPadding = topPadding + bottomPadding
        val horizontalPadding = startPadding + endPadding


        val imageWidth = imageWidthDp.roundToPx()
        val imageHeight = (imageWidth * 4 / 3f).roundToInt()
        val imagePlaceable = measurables.first { it.layoutId == EntryListItemLayoutId.Image }
            .measure(Constraints.fixed(imageWidth, imageHeight))

        val imageOffset = if (imageIgnoresPadding) {
            IntOffset.Zero
        } else IntOffset(startPadding, topPadding)

        val userRateStatusSizePx = UserRateStatusBadgeSize.roundToPx()
        val userRateStatusPlaceable = measurables.firstOrNull {
            it.layoutId == EntryListItemLayoutId.UserRateStatus
        }?.measure(Constraints.fixed(userRateStatusSizePx, userRateStatusSizePx))

        val nameConstraints = Constraints(
            maxWidth = constraints.minWidth - imagePlaceable.width - horizontalPadding - imageOffset.x,
            maxHeight = imageHeight - verticalPadding
        )
        val namePlaceable =
            measurables.first { it.layoutId == EntryListItemLayoutId.Name }.measure(nameConstraints)

        val nameOffset = IntOffset(
            x = imageOffset.x + imagePlaceable.width + startPadding,
            y = topPadding
        )
        val nameSpace = SpaceBetweenNameAndDetailsContent.roundToPx()
        val metadataMeasurable =
            measurables.firstOrNull { it.layoutId == EntryListItemLayoutId.Metadata }
                ?.measure(nameConstraints.copy(maxHeight = nameConstraints.maxHeight - namePlaceable.height - nameSpace))


        val height = if (imageIgnoresPadding) imageHeight else imageHeight + verticalPadding

        return layout(constraints.minWidth, height) {
            imagePlaceable.placeRelative(imageOffset)
            userRateStatusPlaceable?.placeRelative(
                x = imageOffset.x + imageWidth - userRateStatusSizePx,
                y = imageOffset.y
            )

            namePlaceable.placeRelative(nameOffset)
            metadataMeasurable?.placeRelative(
                x = nameOffset.x,
                y = nameOffset.y + namePlaceable.height + nameSpace
            )
        }
    }
}

object EntryListDefaults {
    val ImageWidth = 128.dp
    val ContentPadding = PaddingValues(8.dp)
}

private val UserRateStatusBadgeSize = 32.dp
private val SpaceBetweenNameAndDetailsContent = 2.dp

@Composable
@Preview
fun EntryListItemPreview() {
    ShikimoriTheme {
        EntryListItem(
            name = "Name",
            imageUrl = null,
            onClick = {}
        )
    }
}

@Composable
@Preview
fun EntryListitemWithUserRateStatusPreview() {
    ShikimoriTheme {
        EntryListItem(
            name = "Name",
            imageUrl = null,
            onClick = {},
            userRateStatus = UserRateStatus.Watching
        )
    }
}

@Composable
@Preview
fun EntryListItemWithDetailsContentPreview() {
    ShikimoriTheme {
        EntryListItem(
            name = "Name",
            imageUrl = null,
            onClick = {},
            metadata = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Green)
                )
            }
        )
    }
}

@Composable
@Preview
fun EntryListItemWithBorderAndImageIgnoresPaddingPreview() {
    ShikimoriTheme {
        EntryListItem(
            name = "Name",
            imageUrl = null,
            onClick = {},
            contentPadding = PaddingValues(8.dp),
            border = BorderStroke(1.dp, ShikimoriTheme.colorScheme.outlineVariant),
            containerShape = ShikimoriTheme.shapes.large,
            imageShape = ShikimoriTheme.shapes.large,
            imageIgnoresPadding = true
        )
    }
}

@Composable
@Preview
fun EntryListItemWithBorderAndImageIgnoresPaddingAndDetailsContentPreview() {
    ShikimoriTheme {
        EntryListItem(
            name = "Name",
            imageUrl = null,
            onClick = {},
            contentPadding = PaddingValues(8.dp),
            border = BorderStroke(1.dp, ShikimoriTheme.colorScheme.outlineVariant),
            containerShape = ShikimoriTheme.shapes.large,
            imageShape = ShikimoriTheme.shapes.large,
            imageIgnoresPadding = true,
            metadata = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Green)
                )
            }
        )
    }
}

@Composable
@Preview
fun EntryListItemWithLongNamePaddingPreview() {
    ShikimoriTheme {
        EntryListItem(
            name = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
            imageUrl = null,
            onClick = {},
            contentPadding = PaddingValues(8.dp),
            border = BorderStroke(1.dp, ShikimoriTheme.colorScheme.outlineVariant),
            containerShape = ShikimoriTheme.shapes.large,
            imageShape = ShikimoriTheme.shapes.large,
            imageIgnoresPadding = false
        )
    }
}

@Composable
@Preview
fun EntryListItemWithLongNamePaddingAndDetailsContentPreview() {
    ShikimoriTheme {
        EntryListItem(
            name = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
            imageUrl = null,
            onClick = {},
            contentPadding = PaddingValues(8.dp),
            border = BorderStroke(1.dp, ShikimoriTheme.colorScheme.outlineVariant),
            containerShape = ShikimoriTheme.shapes.large,
            imageShape = ShikimoriTheme.shapes.large,
            imageIgnoresPadding = false,
            metadata = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Green)
                )
            }
        )
    }
}
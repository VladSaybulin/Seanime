package ru.vladsaybulin.core.ui.entry.grid

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import ru.vladsaybulin.core.designsystem.components.drawForegroundGradientScrim
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.UserRateStatusBadge
import ru.vladsaybulin.core.ui.UserRateStatusBadgeDefaults
import ru.vladsaybulin.model.userrate.UserRateStatus
import kotlin.math.roundToInt

@Composable
fun EntryGridItem(
    name: String,
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    nameTextStyle: TextStyle = EntryGridItemDefaults.nameTextStyle,
    bodyTextStyle: TextStyle = EntryGridItemDefaults.bodyTextStyle,
    contentPadding: PaddingValues = EntryGridItemDefaults.contentPadding,
    shape: CornerBasedShape = EntryGridItemDefaults.shape,
    metadata: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val measurePolicy = remember(contentPadding) { EntryGridItemMeasurePolicy(contentPadding) }

    SeanimeTheme(darkTheme = true) {
        Surface(
            onClick = onClick,
            shape = shape,
            shadowElevation = 2.dp,
            modifier = modifier
        ) {
            Layout(
                measurePolicy = measurePolicy,
                content = {
                    EntryGridItemName(name = name, style = nameTextStyle)
                    EntryGridItemImage(imageUrl)
                    UserRateStatusBadge(
                        userRateStatus = userRateStatus,
                        shape = UserRateStatusBadgeDefaults.topEndShape(shape),
                        modifier = Modifier
                            .size(UserRateStatusBadgeSize)
                            .layoutId(EntryGridItemLayoutId.UserRateStatus)
                    )
                    if (metadata != null) {
                        ProvideTextStyle(value = bodyTextStyle) {
                            Column(
                                modifier = Modifier.layoutId(EntryGridItemLayoutId.Metadata),
                                content = metadata
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun EntryGridItemName(name: String, style: TextStyle) {
    Text(
        text = name,
        style = style,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.layoutId(EntryGridItemLayoutId.Name)
    )
}

@Composable
private fun EntryGridItemImage(imageUrl: String?) {
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
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .drawForegroundGradientScrim(
                bottomColor = SeanimeTheme.colorScheme.surface.copy(alpha = GradientScrimAlpha),
                decay = 2f
            )
            .layoutId(EntryGridItemLayoutId.Image)
    )
}

object EntryGridItemDefaults {

    val contentPadding = PaddingValues(
        start = 8.dp,
        end = 8.dp,
        bottom = 4.dp
    )

    val shape
        get() = RoundedCornerShape(16.dp)

    val nameTextStyle
        @Composable get() = SeanimeTheme.typography.labelLarge.copy(color = Color.Unspecified)

    val bodyTextStyle
        @Composable get() = SeanimeTheme.typography.labelSmall

}

private enum class EntryGridItemLayoutId {
    Image, Name, Metadata, UserRateStatus
}

private class EntryGridItemMeasurePolicy(private val contentPadding: PaddingValues) : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {

        val startPadding = contentPadding.calculateStartPadding(layoutDirection).roundToPx()
        val topPadding = contentPadding.calculateTopPadding().roundToPx()
        val endPadding = contentPadding.calculateEndPadding(layoutDirection).roundToPx()
        val bottomPadding = contentPadding.calculateBottomPadding().roundToPx()

        val size: IntSize = when {
            constraints.hasBoundedWidth -> IntSize(
                width = constraints.maxWidth,
                height = (constraints.maxWidth * 4 / 3f).roundToInt()
            )

            constraints.hasBoundedHeight -> IntSize(
                width = (constraints.maxHeight * 3f / 4).roundToInt(),
                height = constraints.maxHeight
            )

            else -> error("EntryGrid size must be constrained to either width or height")
        }

        val imagePlaceable = measurables.first { it.layoutId == EntryGridItemLayoutId.Image }
            .measure(Constraints.fixed(size.width, size.height))

        val userRateStatusSizePx = UserRateStatusBadgeSize.roundToPx()
        val userRateStatusPlaceable = measurables.firstOrNull {
            it.layoutId == EntryGridItemLayoutId.UserRateStatus
        }?.measure(Constraints.fixed(userRateStatusSizePx, userRateStatusSizePx))

        val textConstraints = Constraints(
            minWidth = 0,
            minHeight = 0,
            maxWidth = size.width - startPadding - endPadding,
            maxHeight = size.height - topPadding + bottomPadding
        )

        val namePlaceable = measurables.first { it.layoutId == EntryGridItemLayoutId.Name }
            .measure(textConstraints)

        val metadataPlaceable = measurables.firstOrNull { it.layoutId == EntryGridItemLayoutId.Metadata }
            ?.measure(textConstraints.offset(vertical = -namePlaceable.height))

        return layout(size.width, size.height) {
            imagePlaceable.place(0, 0)
            userRateStatusPlaceable?.placeRelative(size.width - userRateStatusPlaceable.width, 0)

            var y = size.height - bottomPadding
            metadataPlaceable?.let {
                y -= it.height
                it.placeRelative(startPadding, y)
            }

            namePlaceable.placeRelative(startPadding, y - namePlaceable.height)
        }
    }
}

@Composable
@Preview
fun EntryGridItemPreview() {
    SeanimeTheme {
        EntryGridItem(
            name = "Entry name",
            imageUrl = "",
            onClick = { },
            modifier = Modifier.width(150.dp)
        )
    }
}

@Composable
@Preview
fun EntryGridItemWithLongNamePreview() {
    SeanimeTheme {
        EntryGridItem(
            name = "This is a long entry name that does not fit into 2 lines",
            imageUrl = "",
            onClick = { },
            modifier = Modifier.width(150.dp)
        )
    }
}

@Composable
@Preview
fun EntryGridItemWithDetailsPreview() {
    SeanimeTheme {
        EntryGridItem(
            name = "This is a long entry name that does not fit into 2 lines",
            imageUrl = "",
            onClick = { },
            modifier = Modifier.width(150.dp),
            metadata = {
                Text(text = "Details content")
            }
        )
    }
}

@Composable
@Preview
fun EntryGridItemWithUserRateStatusPreview() {
    SeanimeTheme {
        EntryGridItem(
            name = "Entry name",
            imageUrl = "",
            onClick = { },
            modifier = Modifier.width(150.dp),
            userRateStatus = UserRateStatus.Planned,
            metadata = {
                Text(text = "Details content")
            }
        )
    }
}

private const val GradientScrimAlpha = 0.8f
private val UserRateStatusBadgeSize = 32.dp
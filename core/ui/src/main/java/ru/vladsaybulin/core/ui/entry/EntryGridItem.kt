package ru.vladsaybulin.core.ui.entry

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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.UserRateStatusBadge
import ru.vladsaybulin.core.ui.UserRateStatusBadgeDefaults
import ru.vladsaybulin.core.ui.drawForegroundGradientScrim
import ru.vladsaybulin.model.userrate.UserRateStatus
import kotlin.math.roundToInt

@Composable
fun EntryGridItem(
    name: String,
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    nameTextStyle: TextStyle = EntryGridItemDefaults.nameTextStyle,
    bodyTextStyle: TextStyle = EntryGridItemDefaults.bodyTextStyle,
    contentPadding: PaddingValues = EntryGridItemDefaults.contentPadding,
    shape: CornerBasedShape = EntryGridItemDefaults.shape,
    metadata: (@Composable ColumnScope.() -> Unit)? = null,
) {
    ShikimoriTheme(darkTheme = true) {
        Surface(
            onClick = onClick,
            shape = shape,
            shadowElevation = 2.dp
        ) {
            Layout(
                measurePolicy = EntryGridItemMeasurePolicy(contentPadding),
                modifier = modifier,
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
                stopColor = ShikimoriTheme.colorScheme.surface.copy(alpha = GradientScrimAlpha),
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
        @Composable get() = ShikimoriTheme.typography.labelLarge

    val bodyTextStyle
        @Composable get() = ShikimoriTheme.typography.labelSmall

}

private enum class EntryGridItemLayoutId {
    Image, Name, Metadata, UserRateStatus
}

data class EntryGridItemMeasurePolicy(private val contentPadding: PaddingValues) : MeasurePolicy {
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

        val imageWidth = constraints.minWidth
        val imageHeight = (imageWidth * 4 / 3f).roundToInt()
        val imagePlaceable = measurables.first { it.layoutId == EntryGridItemLayoutId.Image }
            .measure(Constraints.fixed(imageWidth, imageHeight))

        val userRateStatusSizePx = UserRateStatusBadgeSize.roundToPx()
        val userRateStatusPlaceable = measurables.firstOrNull {
            it.layoutId == EntryGridItemLayoutId.UserRateStatus
        }?.measure(Constraints.fixed(userRateStatusSizePx, userRateStatusSizePx))

        val textConstraints = Constraints(
            maxWidth = constraints.minWidth - horizontalPadding,
        )

        val metadataPlaceable = measurables.firstOrNull { it.layoutId == EntryGridItemLayoutId.Metadata }
            ?.measure(textConstraints)

        val namePlaceable = measurables.first { it.layoutId == EntryGridItemLayoutId.Name }
                .measure(textConstraints)

        val metadataY = (imageHeight - (metadataPlaceable?.height ?: 0) - bottomPadding)
            .coerceAtLeast(topPadding)
        val nameY = (metadataY - namePlaceable.height).coerceAtLeast(topPadding)

        return layout(imageWidth, imageHeight) {
            imagePlaceable.place(0, 0)
            userRateStatusPlaceable?.placeRelative(imageWidth - userRateStatusPlaceable.width, 0)
            namePlaceable.placeRelative(startPadding, nameY)
            metadataPlaceable?.placeRelative(startPadding, metadataY)
        }
    }
}

@Composable
@Preview
fun EntryGridItemPreview() {
    ShikimoriTheme {
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
    ShikimoriTheme {
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
    ShikimoriTheme {
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
    ShikimoriTheme {
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
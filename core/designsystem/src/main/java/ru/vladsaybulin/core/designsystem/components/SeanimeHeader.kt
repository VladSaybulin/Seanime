package ru.vladsaybulin.core.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun SeanimeHeader(
    modifier: Modifier = Modifier,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    labelTextStyle: TextStyle = SeanimeTitleDefaults.labelTextStyle,
    content: @Composable () -> Unit,
) {
    val decoratedLabel = @Composable {
        ProvideTextStyle(value = labelTextStyle) {
            content()
        }
    }

    Layout(
        content = {
            if (trailing != null) {
                Box(
                    modifier = Modifier
                        .then(IconDefaultSizeModifier)
                        .offset(x = -IconOffsetX)
                        .layoutId(TrailingIconId),
                    contentAlignment = Alignment.Center
                ) {
                    trailing()
                }
            }

            if (leading != null) {
                Box(
                    modifier = Modifier
                        .then(IconDefaultSizeModifier)
                        .offset(x = IconOffsetX)
                        .layoutId(LeadingIconId),
                    contentAlignment = Alignment.Center
                ) {
                    leading()
                }
            }

            val startPadding = if (leading != null) {
                HorizontalLabelPadding - HorizontalIconPadding
            } else {
                HorizontalLabelPadding
            }

            val endPadding = if (trailing != null) {
                HorizontalLabelPadding - HorizontalIconPadding
            } else {
                HorizontalLabelPadding
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = startPadding, end = endPadding)
                    .layoutId(LabelId)
            ) {
                decoratedLabel()
            }
        },
        modifier = modifier,
        measurePolicy = SeanimeTitleMeasurePolicy
    )
}

object SeanimeTitleDefaults {

    val labelTextStyle
        @Composable get() = SeanimeTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
}

object SeanimeTitleMeasurePolicy : MeasurePolicy {
    override fun MeasureScope.measure(measurables: List<Measurable>, constraints: Constraints): MeasureResult {
        val looseConstraint = constraints.copy(minWidth = 0, minHeight = 0)

        var occupiedSpaceHorizontally = 0

        val leadingIconPlaceable = measurables.firstOrNull { it.layoutId == LeadingIconId }
            ?.measure(looseConstraint)
            ?.also { occupiedSpaceHorizontally += it.width }

        val trailingIconPlaceable = measurables.firstOrNull { it.layoutId == TrailingIconId }
            ?.measure(looseConstraint.offset(horizontal = -occupiedSpaceHorizontally))
            ?.also { occupiedSpaceHorizontally += it.width }

        val labelPlaceable = measurables.first { it.layoutId == LabelId }
            .measure(looseConstraint.offset(horizontal = -occupiedSpaceHorizontally))

        val width = calculateWidth(
            leadingIconWidth = leadingIconPlaceable?.width ?: 0,
            labelWidth = labelPlaceable.width,
            trailingIconWidth = trailingIconPlaceable?.width ?: 0,
            constraints = constraints
        )

        val height = calculateHeight(
            leadingIconHeight = leadingIconPlaceable?.height ?: 0,
            labelHeight = labelPlaceable.height,
            trailingIconHeight = trailingIconPlaceable?.height ?: 0,
            density = density,
            constraints = constraints
        )

        return layout(width, height) {
            leadingIconPlaceable?.placeRelative(
                x = 0,
                y = Alignment.CenterVertically.align(height, leadingIconPlaceable.height)
            )

            labelPlaceable.placeRelative(
                x = leadingIconPlaceable?.width ?: 0,
                y = -Alignment.CenterVertically.align(height, labelPlaceable.height)
            )

            trailingIconPlaceable?.placeRelative(
                x = width - trailingIconPlaceable.width,
                y = Alignment.CenterVertically.align(height, trailingIconPlaceable.height)
            )
        }
    }
}

private fun calculateWidth(
    leadingIconWidth: Int,
    labelWidth: Int,
    trailingIconWidth: Int,
    constraints: Constraints
): Int {
    return max(
        constraints.minWidth,
        leadingIconWidth + labelWidth + trailingIconWidth
    )
}


private fun calculateHeight(
    leadingIconHeight: Int,
    labelHeight: Int,
    trailingIconHeight: Int,
    density: Float,
    constraints: Constraints
): Int {
    val contentHeight = max(leadingIconHeight, max(labelHeight, trailingIconHeight))
    val requiredMinHeight = (TitleMinHeight.value * density).roundToInt()
    return max(max(constraints.minHeight, requiredMinHeight), contentHeight)
}

@Preview
@Composable
fun SeanimeTitlePreview() {
    SeanimeTheme {
        Surface {
            SeanimeHeader(
                leading = { Icon(imageVector = SeanimeIcons.CalendarToday, contentDescription = null) },
                trailing = { Icon(imageVector = SeanimeIcons.PlayArrow, contentDescription = null) }
            ) { Text(text = "Title") }
        }
    }
}

@Preview
@Composable
fun SeanimeTitleWithShowLeadingIconOnlyPreview() {
    SeanimeTheme {
        Surface {
            SeanimeHeader(
                leading = { Icon(imageVector = SeanimeIcons.CalendarToday, contentDescription = null) },
            ) { Text(text = "Title") }
        }
    }
}

@Preview
@Composable
fun SeanimeTitleWithTrailingIconPreview() {
    SeanimeTheme {
        Surface {
            SeanimeHeader(
                trailing = { Icon(imageVector = SeanimeIcons.PlayArrow, contentDescription = null) }
            ) { Text(text = "Title") }
        }
    }
}

@Preview
@Composable
fun SeanimeTitleWithoutIconsPreview() {
    SeanimeTheme {
        Surface {
            SeanimeHeader { Text(text = "Title") }
        }
    }
}

private const val LabelId = "label"
private const val LeadingIconId = "leading_icon"
private const val TrailingIconId = "trailing_icon"

private val TitleMinHeight = 48.dp

private val HorizontalLabelPadding = 16.dp
private val HorizontalIconPadding = 12.dp
private val IconOffsetX = 4.dp

private val IconDefaultSizeModifier = Modifier.defaultMinSize(48.dp, 48.dp)
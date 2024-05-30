package ru.vladsaybulin.core.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import kotlin.math.abs

@Composable
fun SeanimeUserRateLinearProgressIndicator(
    progress: Float,
    availableProgress: Float,
    modifier: Modifier = Modifier,
    color: Color = SeanimeLinearProgressIndicatorDefaults.color,
    availableColor: Color = SeanimeLinearProgressIndicatorDefaults.availableColor,
    trackColor: Color = SeanimeLinearProgressIndicatorDefaults.trackColor,
    strokeCap: StrokeCap = SeanimeLinearProgressIndicatorDefaults.strokeCap,
) {
    val coercedProgress = progress.coerceIn(0f, 1f)
    val coercedAvailableProgress = availableProgress.coerceIn(0f, 1f)

    Canvas(
        modifier = modifier
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(coercedProgress, 0f..1f)
            }
            .size(LinearIndicatorWidth, LinearIndicatorHeight)
    ) {
        drawLinearIndicator(
            startFraction = 0f,
            endFraction = 1f,
            color = trackColor,
            strokeWidth = size.height,
            strokeCap = strokeCap
        )

        drawLinearIndicator(
            startFraction = 0f,
            endFraction = coercedAvailableProgress,
            color = availableColor,
            strokeWidth = size.height,
            strokeCap = strokeCap
        )

        drawLinearIndicator(
            startFraction = 0f,
            endFraction = coercedProgress,
            color = color,
            strokeWidth = size.height,
            strokeCap = strokeCap
        )
    }
}

private fun DrawScope.drawLinearIndicator(
    startFraction: Float,
    endFraction: Float,
    color: Color,
    strokeWidth: Float,
    strokeCap: StrokeCap,
) {
    val width = size.width
    val height = size.height
    // Start drawing from the vertical center of the stroke
    val yOffset = height / 2

    val isLtr = layoutDirection == LayoutDirection.Ltr
    val barStart = (if (isLtr) startFraction else 1f - endFraction) * width
    val barEnd = (if (isLtr) endFraction else 1f - startFraction) * width

    // if there isn't enough space to draw the stroke caps, fall back to StrokeCap.Butt
    if (strokeCap == StrokeCap.Butt || height > width) {
        // Progress line
        drawLine(color, Offset(barStart, yOffset), Offset(barEnd, yOffset), strokeWidth)
    } else {
        // need to adjust barStart and barEnd for the stroke caps
        val strokeCapOffset = strokeWidth / 2
        val coerceRange = strokeCapOffset..(width - strokeCapOffset)
        val adjustedBarStart = barStart.coerceIn(coerceRange)
        val adjustedBarEnd = barEnd.coerceIn(coerceRange)

        if (abs(endFraction - startFraction) > 0) {
            // Progress line
            drawLine(
                color,
                Offset(adjustedBarStart, yOffset),
                Offset(adjustedBarEnd, yOffset),
                strokeWidth,
                strokeCap,
            )
        }
    }
}

object SeanimeLinearProgressIndicatorDefaults {
    val color: Color
        @Composable get() = SeanimeTheme.colorScheme.primary

    val availableColor: Color
        @Composable get() = SeanimeTheme.colorScheme.primary.copy(alpha = AVAILABLE_COLOR_OPACITY)

    val trackColor: Color
        @Composable get() = SeanimeTheme.colorScheme.surfaceVariant

    val strokeCap = StrokeCap.Round

}

@Preview
@Composable
fun SeanimeLinearProgressIndicatorPreview() {
    SeanimeTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                SeanimeUserRateLinearProgressIndicator(
                    progress = 0.4f,
                    availableProgress = 0.8f
                )
            }
        }
    }
}

private val LinearIndicatorWidth = 240.dp
private val LinearIndicatorHeight = 6.dp

private const val AVAILABLE_COLOR_OPACITY = 0.2f
package ru.vladsaybulin.core.ui2.score

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp.Companion.Difference
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import kotlin.math.min

/**
 * A row of stars representing a score.
 *
 * @param score The score to display, usually in range 0..10.
 * @param modifier The modifier to be applied to the layout.
 * @param iconSize The size of each star icon.
 * @param tint The color of the filled stars.
 */
@Composable
fun StarsRow(
    score: Float,
    modifier: Modifier = Modifier,
    iconSize: Dp = ScoreDefaults.IconSize,
    tint: Color = ScoreDefaults.IconTintColor
) {
    val clampedScore = score.coerceIn(0f, MAX_SCORE)

    val filledStar = rememberVectorPainter(image = SeanimeIcons.Star)
    val outlineStar = rememberVectorPainter(image = SeanimeIcons.StarOutline)

    val outlineStarColor = SeanimeTheme.colorScheme.outline
    val contentDescription = stringResource(R.string.core_ui2_score_stars_row_content_description, clampedScore)

    Canvas(
        modifier = modifier
            .semantics {
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = clampedScore,
                    range = 0f..MAX_SCORE
                )
                stateDescription = contentDescription
            }
            .size(width = iconSize * STARS_AMOUNT, height = iconSize)
    ) {
        val filledColorFilter = ColorFilter.tint(tint)
        val outlineColorFilter = ColorFilter.tint(outlineStarColor)

        drawStars(
            score = clampedScore,
            drawFilled = {
                with(filledStar) { draw(size = size, colorFilter = filledColorFilter) }
            },
            drawOutline = {
                with(outlineStar) { draw(size = size, colorFilter = outlineColorFilter) }
            }
        )
    }
}

/**
 * An interactive version of [StarsRow] that allows users to change the score by clicking or dragging.
 *
 * @param score The current integer score (0..10).
 * @param onChanged Callback invoked when the user selects a new score.
 * @param modifier The modifier to be applied to the layout.
 * @param iconSize The size of each star icon.
 * @param tint The color of the filled stars.
 */
@Composable
fun StarsRowInput(
    score: Int,
    onChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = ScoreDefaults.IconSize,
    tint: Color = ScoreDefaults.IconTintColor
) {
    val animatedScore by animateFloatAsState(targetValue = score.toFloat())

    StarsRow(
        score = animatedScore,
        modifier = modifier.starsInput(score, onChanged),
        iconSize = iconSize,
        tint = tint
    )
}

/**
 * Extension function for [DrawScope] to draw a row of stars.
 *
 * @param score The score value to draw.
 * @param drawFilled A block to draw a filled star.
 * @param drawOutline A block to draw an outlined star.
 */
fun DrawScope.drawStars(
    score: Float,
    drawFilled: DrawScope.() -> Unit,
    drawOutline: DrawScope.() -> Unit
) {
    val stars = score.coerceIn(0f, MAX_SCORE) / SCORE_POINTS_PER_STAR
    val fullStars = stars.toInt()
    val partiallyFilledFraction = stars - fullStars
    val outlineStars = (STARS_AMOUNT - stars).toInt()

    val starSize = min(size.width / STARS_AMOUNT, size.height).let { Size(it, it) }
    val dx: Float = if (layoutDirection == LayoutDirection.Ltr) {
        starSize.width
    } else {
        drawContext.transform.translate(left = size.width - starSize.width)
        -starSize.width
    }
    drawContext.size = starSize

    repeat(fullStars) {
        drawFilled()
        drawContext.transform.translate(left = dx)
    }

    if (partiallyFilledFraction > 0) {
        drawOutline()
        val iconSpacePx = starSize.width / 8f
        val drawingSize = starSize.width - iconSpacePx * 2
        if (layoutDirection == LayoutDirection.Ltr) {
            val clipPx = drawingSize * partiallyFilledFraction + iconSpacePx
            clipRect(
                left = 0f,
                right = clipPx,
                block = drawFilled
            )
        } else {
            val clipPx = drawingSize * (1f - partiallyFilledFraction) + iconSpacePx
            clipRect(
                left = clipPx,
                right = 0f,
                clipOp = Difference,
                block = drawFilled
            )
        }

        drawContext.transform.translate(left = dx)
    }

    repeat(outlineStars) {
        drawOutline()
        drawContext.transform.translate(left = dx)
    }
}

class ScorePreviewParameterProvider : PreviewParameterProvider<Float> {
    override val values: Sequence<Float> = sequenceOf(0f, 3f, 8f, 10f, 7.5f)
}

@Composable
@Preview
fun StarsRowLtrPreview(
    @PreviewParameter(ScorePreviewParameterProvider::class) score: Float
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        SeanimeTheme {
            StarsRow(score = score)
        }
    }
}

@Composable
@Preview
fun StarsRowRtlPreview(
    @PreviewParameter(ScorePreviewParameterProvider::class) score: Float
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        SeanimeTheme {
            StarsRow(score = score)
        }
    }
}

@Composable
@Preview
fun StarsRowInLargeContainerPreview() {
    SeanimeTheme {
        Box(
            modifier = Modifier.width(ScoreDefaults.IconSize * STARS_AMOUNT * 2)
        ) {
            StarsRow(score = 5f)
        }
    }
}

@Composable
@Preview
fun StarsRowInputPreview() {
    SeanimeTheme {
        var score by remember { mutableIntStateOf(7) }
        StarsRowInput(score = score, onChanged = { score = it })
    }
}

private const val STARS_AMOUNT: Int = 5
internal const val MAX_SCORE: Float = 10f
private const val SCORE_POINTS_PER_STAR: Float = MAX_SCORE / STARS_AMOUNT

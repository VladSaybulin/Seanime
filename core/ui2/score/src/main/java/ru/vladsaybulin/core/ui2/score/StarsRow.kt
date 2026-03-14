package ru.vladsaybulin.core.ui2.score

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp.Companion.Difference
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import kotlin.math.min

@Composable
fun StarsRow(
    score: Float,
    modifier: Modifier = Modifier,
    iconSize: Dp = ScoreStarsDefaults.Size,
    tint: Color = ScoreStarsDefaults.Tint
) {
    val filledStar = rememberVectorPainter(image = SeanimeIcons.Star)
    val outlineStar = rememberVectorPainter(image = SeanimeIcons.StarOutline)

    val outlineStarColor = SeanimeTheme.colorScheme.outline

    Canvas(
        modifier = modifier.defaultMinSize(minWidth = iconSize * STARS_AMOUNT, minHeight = iconSize).aspectRatio(STARS_AMOUNT.toFloat())
    ) {
        val filledColorFilter = ColorFilter.tint(tint)
        val outlineColorFilter = ColorFilter.tint(outlineStarColor)

        drawStars(
            score = score,
            drawFilled = {
                with(filledStar) { draw(size = size, colorFilter = filledColorFilter) }
            },
            drawOutline = {
                with(outlineStar) { draw(size = size, colorFilter = outlineColorFilter) }
            }
        )
    }
}

object ScoreStarsDefaults {
    val Size = 40.dp

    val Tint: Color
        @Composable get() = SeanimeTheme.colorScheme.primary
}

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

private const val STARS_AMOUNT: Int = 5
private const val MAX_SCORE: Float = 10f
private const val SCORE_POINTS_PER_STAR: Float = MAX_SCORE / STARS_AMOUNT
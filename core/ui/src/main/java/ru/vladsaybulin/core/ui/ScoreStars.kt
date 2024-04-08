package ru.vladsaybulin.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import java.util.Random

@Composable
fun AnimatedScoreStars(
    score: Float,
    modifier: Modifier = Modifier,
    starSize: DpSize = DefaultStarSize,
    color: Color = ShikimoriTheme.colorScheme.primary,
) {
    val animatedScore by animateFloatAsState(
        targetValue = score,
        label = "ScoreStars"
    )

    ScoreStars(
        score = animatedScore,
        modifier = modifier,
        starSize = starSize,
        color = color
    )
}

@Composable
private fun ScoreStars(
    score: Float,
    modifier: Modifier = Modifier,
    starSize: DpSize = DefaultStarSize,
    color: Color = ShikimoriTheme.colorScheme.primary,
) {
    val filledStar = rememberVectorPainter(image = ShikimoriIcons.Star)
    val outlinedStar = rememberVectorPainter(image = ShikimoriIcons.StarOutline)

    val outlinedColor = ShikimoriTheme.colorScheme.outlineVariant

    Canvas(
        modifier = modifier.size(
            DpSize(
                width = starSize.width * StarsAmount,
                height = starSize.height
            )
        )
    ) {
        val filledColorFilter = ColorFilter.tint(color)
        val outlinedColorFilter = ColorFilter.tint(outlinedColor)

        val stars = score / ScorePointsPerStar
        val full = stars.toInt()
        val notFull = stars - full

        val starWidth = size.width / StarsAmount
        val starSizePx = Size(starWidth, size.height)

        with(filledStar) {
            for (i in 0..<full) {
                draw(size = starSizePx, colorFilter = filledColorFilter)
                drawContext.transform.translate(left = starWidth)
            }
            if (notFull > 0) {
                clipRect(right = starWidth * notFull) {
                    draw(size = starSizePx, colorFilter = filledColorFilter)
                }
            }
        }

        if (full == MaxScore) return@Canvas

        with(outlinedStar) {
            if (notFull > 0) {
                clipRect(left = starWidth * notFull) {
                    draw(size = starSizePx, colorFilter = outlinedColorFilter)
                }
                drawContext.transform.translate(left = starWidth)
            }

            val start = if (notFull == 0f) full else full + 1
            for (i in start..<StarsAmount) {
                draw(size = starSizePx, colorFilter = outlinedColorFilter)
                drawContext.transform.translate(left = starWidth)
            }
        }
    }
}

@Preview
@Composable
fun ScoreStarsPreview() {
    ShikimoriTheme {
        Surface {
            Column {
                val score = remember {
                    mutableIntStateOf(7)
                }

                AnimatedScoreStars(score = score.intValue.toFloat())

                Button(onClick = { score.intValue = Random().nextInt(10) }) {
                    Text(text = "Change")
                }
            }

        }
    }
}

val DefaultStarSize = DpSize(40.dp, 40.dp)

private const val StarsAmount = 5
private const val MaxScore = 10
private const val ScorePointsPerStar = MaxScore.toFloat() / StarsAmount
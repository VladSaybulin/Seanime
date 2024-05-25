package ru.vladsaybulin.core.ui.score

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import java.util.Random
import kotlin.math.roundToInt

@Composable
fun AnimatedScoreStars(
    score: Float,
    modifier: Modifier = Modifier,
    starSize: DpSize = DefaultStarSize,
    color: Color = SeanimeTheme.colorScheme.primary,
) {
    val animatedScore by animateFloatAsState(
        targetValue = score,
        animationSpec = tween(),
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
fun ScoreStars(
    score: Float,
    modifier: Modifier = Modifier,
    starSize: DpSize = DefaultStarSize,
    color: Color = SeanimeTheme.colorScheme.primary,
) {
    val filledStar = rememberVectorPainter(image = ShikimoriIcons.Star)
    val outlinedStar = rememberVectorPainter(image = ShikimoriIcons.StarOutline)

    val outlinedColor = SeanimeTheme.colorScheme.outlineVariant

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

fun Modifier.inputScore(
    onScoreChange: (Int) -> Unit
) = this then InputScoreElement(onScoreChange)

data class InputScoreElement(
    val onScoreChange: (Int) -> Unit
) : ModifierNodeElement<InputScoreNode>() {
    override fun create(): InputScoreNode = InputScoreNode(onScoreChange)

    override fun update(node: InputScoreNode) {
        node.onScoreChange = onScoreChange
    }

    override fun InspectorInfo.inspectableProperties() {
        properties["onScoreChange"] = onScoreChange
    }
}

class InputScoreNode(
    var onScoreChange: (Int) -> Unit
) : PointerInputModifierNode, Modifier.Node() {

    private var isPressed = false

    override fun onCancelPointerInput() {
        isPressed = false
    }

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize
    ) {
        if (pass != PointerEventPass.Main) return
        when (pointerEvent.type) {
            PointerEventType.Press -> {
                isPressed = true
            }
            PointerEventType.Release -> {
                isPressed = false
            }
        }

        if (!isPressed) return
        val change = pointerEvent.changes.first()
        change.consume()
        updateScore(change.position, bounds)
    }

    private fun updateScore(position: Offset, bounds: IntSize) {
        val score = ((position.x / bounds.width) * MaxScore).roundToInt()
            .coerceIn(0..MaxScore)
        onScoreChange(score)
    }
}

@Preview
@Composable
fun ScoreStarsPreview() {
    SeanimeTheme {
        Surface {
            Column {
                val score = remember { mutableIntStateOf(7) }

                AnimatedScoreStars(score = score.intValue.toFloat())

                Button(onClick = { score.intValue = Random().nextInt(10) }) {
                    Text(text = "Change")
                }
            }
        }
    }
}

val DefaultStarSize = DpSize(40.dp, 40.dp)
val SmallStarSize = DpSize(24.dp, 24.dp)

private const val StarsAmount = 5
private const val MaxScore = 10
private const val ScorePointsPerStar = MaxScore.toFloat() / StarsAmount
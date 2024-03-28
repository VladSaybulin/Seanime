package ru.vladsaybulin.feature.userrate.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import kotlin.math.roundToInt

sealed class StarState {
    data object Filled : StarState()

    data class Fraction(val fraction: Float) : StarState()

    data object Outlined : StarState()
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun rememberScoreRowState(
    initialScore: Int = 0,
    starSizePx: Size = LocalDensity.current.run { StarSizeDp.toSize() },
    maxScore: Int = DEFAULT_MAX_SCORE,
    starCount: Int = DEFAULT_COUNT_STAR
): ScoreRowState {
    require(initialScore <= maxScore) { "initialScore > maxScore" }

    val density = LocalDensity.current
    return remember {
        ScoreRowState(
            draggableState = AnchoredDraggableState(
                initialValue = initialScore,
                anchors = DraggableAnchors {
                    (0..maxScore).forEach { it at starSizePx.width * it * 0.5f }
                },
                positionalThreshold = { it * 0.5f },
                velocityThreshold = { with(density) { 10000.dp.toPx() } },
                animationSpec = tween()
            ),
            initialStarState = starSizePx,
            starCount = starCount,
            maxScore = maxScore
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
class ScoreRowState(
    val draggableState: AnchoredDraggableState<Int>,
    initialStarState: Size,
    val maxScore: Int,
    val starCount: Int
) {
    val selectedScore: Int
        get() = draggableState.targetValue

    private val _starSizePx = mutableStateOf(initialStarState)
    var starSizePx
        get() = _starSizePx.value
        set(value) {
            _starSizePx.value = value
        }

    val starStates: List<StarState>
        get() {
            val width = starSizePx.width
            var remaining = draggableState.requireOffset()
            return List(starCount) {
                when {
                    remaining <= 0 -> StarState.Outlined
                    remaining <= width -> StarState.Fraction(remaining)
                    else -> StarState.Filled
                }.also {
                    remaining -= width
                }
            }
        }
}

fun Modifier.scoreRowPointerInput(
    maxScore: Int = DEFAULT_MAX_SCORE,
    onScoreChanged: (Int) -> Unit,
) = this then ScoreRowPointerInputModifierElement(onScoreChanged, maxScore)

data class ScoreRowPointerInputModifierElement(
    val onScoreChanged: (Int) -> Unit,
    val maxScore: Int
) : ModifierNodeElement<ScoreRowPointerInputModifierNode>() {
    override fun create(): ScoreRowPointerInputModifierNode = ScoreRowPointerInputModifierNode(
        onScoreChanged = onScoreChanged,
        maxScore = maxScore
    )

    override fun update(node: ScoreRowPointerInputModifierNode) {
        node.onScoreChanged = onScoreChanged
        node.maxScore = maxScore
    }
}

class ScoreRowPointerInputModifierNode(
    var onScoreChanged: (Int) -> Unit,
    var maxScore: Int
) : Modifier.Node(), PointerInputModifierNode {

    var pressPos: Offset? = null

    override fun onCancelPointerInput() {
        pressPos = null
    }

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize
    ) {
        if (pass != PointerEventPass.Main) return
        if (pointerEvent.type == PointerEventType.Press) {
            pressPos = pointerEvent.changes.first().position
            return
        }
        if (pointerEvent.type == PointerEventType.Release && pressPos != null) {
            val releasePos = pointerEvent.changes.first().position
            if (releasePos != pressPos) {
                pressPos = null
                return
            }
            val score = (releasePos.x / bounds.width * maxScore).roundToInt()
            onScoreChanged(score)
            pressPos = null
            return
        }
        pressPos = null
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScoreRow(
    state: ScoreRowState,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val starFilledPainter = rememberVectorPainter(image = ShikimoriIcons.Star)
    val starOutlinePainter = rememberVectorPainter(image = ShikimoriIcons.StarOutline)

    val starSizePx = with(density) { StarSizeDp.toSize() }
    val canvasSizeDp = DpSize(StarSizeDp.width * 5, StarSizeDp.height)

    val scope = rememberCoroutineScope()
    val color = lerp(ShikimoriTheme.colorScheme.error, Color.Green, state.selectedScore / 10f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .size(canvasSizeDp)
                .scoreRowPointerInput { score ->
                    scope.launch { state.draggableState.animateTo(score) }
                }
                .anchoredDraggable(state.draggableState, Orientation.Horizontal)
        ) {
            val starStates = state.starStates
            starStates.forEachIndexed { index, state ->
                translate(left = index * starSizePx.width) {
                    when (state) {
                        StarState.Filled -> drawPainter(starFilledPainter, starSizePx, color)
                        StarState.Outlined -> drawPainter(starOutlinePainter, starSizePx, color)
                        is StarState.Fraction -> {
                            clipRect(right = state.fraction) {
                                drawPainter(starFilledPainter, starSizePx, color)
                            }
                            clipRect(left = state.fraction) {
                                drawPainter(starOutlinePainter, starSizePx, color)
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = state.selectedScore.toString(),
            style = ShikimoriTheme.typography.headlineLarge,
            color = color
        )
    }
}

fun DrawScope.drawPainter(
    painter: Painter,
    size: Size,
    color: Color
) {
    with(painter) {
        draw(
            size = size,
            colorFilter = ColorFilter.tint(color)
        )
    }
}

@Preview
@Composable
fun ScoreRowPreview() {
    ShikimoriTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ScoreRow(state = rememberScoreRowState(initialScore = 0))
        }
    }
}

private val StarSizeDp = DpSize(56.dp, 56.dp)

private const val DEFAULT_MAX_SCORE = 10
private const val DEFAULT_COUNT_STAR = 5
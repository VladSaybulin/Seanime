package ru.vladsaybulin.feature.userrate.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.SuspendingPointerInputModifierNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import kotlin.math.roundToInt

class ScoreState(initialScore: Int) {

    private var _animatedScoreProgress =
        Animatable(initialScore.toFloat()).apply {
            updateBounds(0f, MAX_SCORE.toFloat())
        }

    val scoreProgress: Float
        get() = _animatedScoreProgress.value

    val targetScore: Int
        get() = _animatedScoreProgress.targetValue.toInt()

    suspend fun animateTo(score: Int) {
        this._animatedScoreProgress.animateTo(score.toFloat(), animationSpec = tween())
    }
}

@Composable
fun ScoreRow(
    state: ScoreState,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val starFilledPainter = rememberVectorPainter(image = ShikimoriIcons.Star)
    val starOutlinePainter = rememberVectorPainter(image = ShikimoriIcons.StarOutline)

    val starSizePx = with(density) { StarSizeDp.toSize() }
    val canvasSizeDp = DpSize(StarSizeDp.width * 5, StarSizeDp.height)

    val outlineColor = ShikimoriTheme.colorScheme.outline
    val color = if (state.targetScore > 0f) {
        lerp(
            start = ShikimoriTheme.colorScheme.error,
            stop = ShikimoriTheme.userRateColors.completed,
            fraction = state.scoreProgress / MAX_SCORE
        )
    } else outlineColor

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Canvas(
            modifier = Modifier
                .size(canvasSizeDp)
                .scoreContainerPointerEvent(state)
        ) {
            drawStars(
                scoreProgress = state.scoreProgress,
                filledStarPainter = starFilledPainter,
                filledStarColorFilter = ColorFilter.tint(color),
                outlinedStarPainter = starOutlinePainter,
                outlinedStarColorFilter = ColorFilter.tint(outlineColor),
                starSize = starSizePx
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = state.targetScore.toString(),
            style = ShikimoriTheme.typography.headlineLarge,
            color = color
        )
    }
}

fun DrawScope.drawStars(
    scoreProgress: Float,
    filledStarPainter: Painter,
    filledStarColorFilter: ColorFilter,
    outlinedStarPainter: Painter,
    outlinedStarColorFilter: ColorFilter,
    starSize: Size,
) {
    val scoresPerStar = MAX_SCORE.toFloat() / STAR_COUNT.toFloat()
    val fullStars = (scoreProgress / scoresPerStar).toInt()

    (1..fullStars).forEach { _ ->
        with(filledStarPainter) {
            draw(
                size = starSize,
                colorFilter = filledStarColorFilter
            )
        }
        drawContext.transform.translate(left = starSize.width)
    }

    if (fullStars == STAR_COUNT) return

    val notFullStarProgress = (scoreProgress - (fullStars * scoresPerStar)) / scoresPerStar
    val clipFraction = notFullStarProgress * starSize.width
    clipRect(right = clipFraction) {
        with(filledStarPainter) {
            draw(
                size = starSize,
                colorFilter = filledStarColorFilter
            )
        }
    }
    clipRect(left = clipFraction) {
        with(outlinedStarPainter) {
            draw(
                size = starSize,
                colorFilter = outlinedStarColorFilter
            )
        }
    }

    drawContext.transform.translate(left = starSize.width)

    ((fullStars + 1)..<STAR_COUNT).forEach { _ ->
        with(outlinedStarPainter) {
            draw(
                size = starSize,
                colorFilter = outlinedStarColorFilter
            )
        }
        drawContext.transform.translate(left = starSize.width)
    }
}

fun Modifier.scoreContainerPointerEvent(state: ScoreState) =
    this then ScoreContainerModifierElement(state)

data class ScoreContainerModifierElement(
    val state: ScoreState
) : ModifierNodeElement<ScoreContainerModifierNode>() {

    override fun create(): ScoreContainerModifierNode = ScoreContainerModifierNode(state)

    override fun update(node: ScoreContainerModifierNode) {
        node.setState(state)
    }

    override fun InspectorInfo.inspectableProperties() {
        properties["state"] = state
    }
}

class ScoreContainerModifierNode(
    initialState: ScoreState
) : PointerInputModifierNode, DelegatingNode() {

    private var state = initialState

    private var currentProgress: Float = state.targetScore / MAX_SCORE.toFloat()
        set(value) {
            field = value.coerceIn(0f..1f)
        }

    private val dragGesturesPointerInput = delegate(SuspendingPointerInputModifierNode {
        detectHorizontalDragGestures { change, dragAmount ->
            currentProgress += dragAmount / size.width
            onProgressChanged()
        }
    })

    override fun onCancelPointerInput() {
        dragGesturesPointerInput.onCancelPointerInput()
    }

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize
    ) {
        dragGesturesPointerInput.onPointerEvent(pointerEvent, pass, bounds)
    }

    private fun onProgressChanged() {
        val targetScore = (currentProgress * MAX_SCORE).roundToInt()
        if (state.targetScore != targetScore)
            coroutineScope.launch {
                state.animateTo(targetScore)
            }
    }

    fun setState(state: ScoreState) {
        this.state = state
        currentProgress = state.targetScore / MAX_SCORE.toFloat()
    }
}

@Preview
@Composable
fun ScoreRowPreview() {
    ShikimoriTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val state = remember { ScoreState(5) }
            ScoreRow(state = state)
        }
    }
}

private val StarSizeDp = DpSize(40.dp, 40.dp)

private const val MAX_SCORE = 10
private const val STAR_COUNT = 5
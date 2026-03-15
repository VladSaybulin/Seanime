package ru.vladsaybulin.core.ui2.score

import android.annotation.SuppressLint
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt

/**
 * Adds star-based drag input and exposes accessibility semantics for score editing.
 *
 * [currentScore] is the source of truth for semantics and deduplication.
 *
 * Reports score changes in the [0..[MAX_SCORE_POINTS]] range via [onScoreChanged].
 * The callback is invoked only when calculated score differs from the latest known score.
 *
 * Semantics contract:
 * - `progressBarRangeInfo` is set to `0f..[MAX_SCORE_POINTS]`.
 * - `stateDescription` is exposed as `"x/[MAX_SCORE_POINTS]"`.
 * - `setProgress` maps to integer score updates and forwards them to [onScoreChanged].
 */
@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.starsInput(
    currentScore: Int,
    onScoreChanged: (newScore: Int) -> Unit
): Modifier = composed {
    // TODO: After upgrading Compose, remove composed and read layout direction in Node API.
    val layoutDirection = LocalLayoutDirection.current
    val clampedScore = currentScore.coerceIn(0, MAX_SCORE_POINTS)
    this
        .semantics {
            val range = 0f..MAX_SCORE_POINTS.toFloat()
            progressBarRangeInfo = ProgressBarRangeInfo(current = clampedScore.toFloat(), range = range)
            stateDescription = "$clampedScore/$MAX_SCORE_POINTS"
            setProgress { targetValue ->
                val newScore = targetValue.roundToInt().coerceIn(0, MAX_SCORE_POINTS)
                if (newScore == clampedScore) {
                    false
                } else {
                    onScoreChanged(newScore)
                    true
                }
            }
        }
        .then(
            StarsInputElement(
                currentScore = clampedScore,
                onScoreChanged = onScoreChanged,
                layoutDirection = layoutDirection
            )
        )
}

/**
 * Modifier element that keeps score/callback/layout direction and updates [StarsInputNode] efficiently.
 */
class StarsInputElement @SuppressLint("ModifierFactoryReturnType") constructor(
    private val currentScore: Int,
    private val onScoreChanged: (newScore: Int) -> Unit,
    private val layoutDirection: LayoutDirection
) : ModifierNodeElement<StarsInputNode>() {
    override fun create(): StarsInputNode = StarsInputNode(currentScore, onScoreChanged, layoutDirection)

    override fun update(node: StarsInputNode) {
        node.update(currentScore, onScoreChanged, layoutDirection)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "starsInput"
        properties["currentScore"] = currentScore
        properties["onScoreChanged"] = onScoreChanged
    }

    override fun hashCode(): Int {
        var result = currentScore
        result = 31 * result + onScoreChanged.hashCode()
        result = 31 * result + layoutDirection.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other === null) return false
        if (other::class != this::class) return false

        other as StarsInputElement

        return currentScore == other.currentScore &&
            onScoreChanged === other.onScoreChanged &&
            layoutDirection == other.layoutDirection
    }
}

/**
 * Pointer-input node for stars score editing.
 *
 * It tracks a single active pointer during press-drag-release, supports RTL coordinate mapping,
 * ignores secondary presses while a drag is active, and deduplicates score emissions.
 */
class StarsInputNode(
    currentScore: Int,
    private var onScoreChanged: (newScore: Int) -> Unit,
    private var layoutDirection: LayoutDirection
) : Modifier.Node(), PointerInputModifierNode {

    private var isPressed = false
    private var activePointerId: PointerId? = null
    private var currentScore: Int = currentScore.coerceIn(0, MAX_SCORE_POINTS)

    fun update(currentScore: Int, onScoreChanged: (newScore: Int) -> Unit, layoutDirection: LayoutDirection) {
        val coercedScore = currentScore.coerceIn(0, MAX_SCORE_POINTS)
        if (this.currentScore != coercedScore) {
            this.currentScore = coercedScore
        }
        this.onScoreChanged = onScoreChanged
        this.layoutDirection = layoutDirection
    }

    override fun onCancelPointerInput() {
        resetPointerState()
    }

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize
    ) {
        if (pass != PointerEventPass.Main) return

        val activeChange = pointerEvent.changes.firstOrNull { it.id == activePointerId }
        when (pointerEvent.type) {
            PointerEventType.Press -> {
                if (isPressed && activePointerId != null) return
                val pressChange = pointerEvent.changes.firstOrNull() ?: return
                activePointerId = pressChange.id
                isPressed = true
                updateScore(pressChange.position, bounds)?.let { score ->
                    pressChange.consume()
                    emitScoreIfChanged(score)
                }
            }

            PointerEventType.Move -> {
                if (!isPressed) return
                val moveChange = activeChange ?: return
                updateScore(moveChange.position, bounds)?.let { score ->
                    moveChange.consume()
                    emitScoreIfChanged(score)
                }
            }

            PointerEventType.Release -> {
                val releaseChange = activeChange ?: return
                updateScore(releaseChange.position, bounds)?.let { score ->
                    releaseChange.consume()
                    emitScoreIfChanged(score)
                }
                resetPointerState()
            }

            else -> {}
        }
    }

    private fun resetPointerState() {
        isPressed = false
        activePointerId = null
    }

    /** Maps pointer position to a clamped score value. */
    private fun updateScore(position: Offset, bounds: IntSize): Int? {
        if (bounds.width <= 0) return null
        val relativeX = if (layoutDirection == LayoutDirection.Rtl) {
            bounds.width - position.x
        } else {
            position.x
        }
        return ((relativeX / bounds.width) * MAX_SCORE_POINTS).roundToInt()
            .coerceIn(0, MAX_SCORE_POINTS)
    }

    /** Emits score only when it differs from the previously emitted value. */
    private fun emitScoreIfChanged(score: Int) {
        if (currentScore == score) return
        currentScore = score
        onScoreChanged(score)
    }
}

/** Max editable score value for stars input. */
private const val MAX_SCORE_POINTS: Int = 10

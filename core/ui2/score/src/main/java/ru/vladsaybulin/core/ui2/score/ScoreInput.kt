package ru.vladsaybulin.core.ui2.score

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt

/**
 * Applies a pointer input listener to handle score selection via stars.
 * Supports tap and drag gestures.
 *
 * @param currentScore The current score value (0..10).
 * @param onScoreChanged Callback for when a new score is selected.
 */
internal fun Modifier.starsInput(
    currentScore: Int,
    onScoreChanged: (newScore: Int) -> Unit
): Modifier {
    val clampedScore = currentScore.coerceIn(0, MAX_SCORE.toInt())
    return this
        .semantics {
            setProgress { targetValue ->
                val newScore = targetValue.roundToInt().coerceIn(0, MAX_SCORE.toInt())
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
            )
        )
}

private class StarsInputElement constructor(
    private val currentScore: Int,
    private val onScoreChanged: (newScore: Int) -> Unit,
) : ModifierNodeElement<StarsInputNode>() {
    override fun create(): StarsInputNode = StarsInputNode(currentScore, onScoreChanged)

    override fun update(node: StarsInputNode) {
        node.update(currentScore, onScoreChanged)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "starsInput"
        properties["currentScore"] = currentScore
        properties["onScoreChanged"] = onScoreChanged
    }

    override fun hashCode(): Int {
        var result = currentScore
        result = 31 * result + onScoreChanged.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other === null) return false
        if (other::class != this::class) return false

        other as StarsInputElement

        return currentScore == other.currentScore &&
                onScoreChanged === other.onScoreChanged
    }
}

/**
 * Pointer-input node for stars score editing.
 *
 * It tracks a single active pointer during press-drag-release, supports RTL coordinate mapping,
 * ignores secondary presses while a drag is active, and deduplicates score emissions.
 */
private class StarsInputNode(
    initialScore: Int,
    private var onScoreChanged: (newScore: Int) -> Unit,
) : Modifier.Node(), PointerInputModifierNode, CompositionLocalConsumerModifierNode {
    private var isPressed = false
    private var activePointerId: PointerId? = null
    private var currentScore: Int = initialScore

    fun update(currentScore: Int, onScoreChanged: (newScore: Int) -> Unit) {
        val coercedScore = currentScore.coerceIn(0, MAX_SCORE.toInt())
        if (this.currentScore != coercedScore) {
            this.currentScore = coercedScore
        }
        this.onScoreChanged = onScoreChanged
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

    private fun updateScore(position: Offset, bounds: IntSize): Int? {
        val layoutDirection = currentValueOf(LocalLayoutDirection)

        if (bounds.width <= 0) return null
        val relativeX = if (layoutDirection == LayoutDirection.Rtl) {
            bounds.width - position.x
        } else {
            position.x
        }
        return ((relativeX / bounds.width) * MAX_SCORE).roundToInt()
            .coerceIn(0, MAX_SCORE.toInt())
    }

    private fun emitScoreIfChanged(score: Int) {
        if (currentScore == score) return
        currentScore = score
        onScoreChanged(score)
    }
}

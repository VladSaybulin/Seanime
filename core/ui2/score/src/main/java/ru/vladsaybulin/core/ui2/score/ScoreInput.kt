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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt

/**
 * Adds star-based drag input and reports score changes in the [0..MAX_SCORE_POINTS] range.
 *
 * The callback is invoked only when calculated score changes.
 */
fun Modifier.starsInput(
    onScoreChanged: (newScore: Int) -> Unit
): Modifier = @SuppressLint("UnnecessaryComposedModifier") composed {
    // TODO: After upgrading Compose, remove composed and read layout direction in Node API.
    val layoutDirection = LocalLayoutDirection.current
    this then StarsInputElement(onScoreChanged, layoutDirection)
}

/**
 * Modifier element that keeps callback/layout direction and updates [StarsInputNode] efficiently.
 */
class StarsInputElement @SuppressLint("ModifierFactoryReturnType") constructor(
    private val onScoreChanged: (newScore: Int) -> Unit,
    private val layoutDirection: LayoutDirection
) : ModifierNodeElement<StarsInputNode>() {
    override fun create(): StarsInputNode = StarsInputNode(onScoreChanged, layoutDirection)

    override fun update(node: StarsInputNode) {
        node.update(onScoreChanged, layoutDirection)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "starsInput"
        properties["onScoreChanged"] = onScoreChanged
    }

    override fun hashCode(): Int = 31 * onScoreChanged.hashCode() + layoutDirection.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other === null) return false
        if (other::class != this::class) return false

        other as StarsInputElement

        return onScoreChanged === other.onScoreChanged &&
            layoutDirection == other.layoutDirection
    }
}

/**
 * Pointer-input node for stars score editing.
 *
 * It tracks a single active pointer during press-drag-release, supports RTL coordinate mapping,
 * and deduplicates score emissions.
 */
class StarsInputNode(
    private var onScoreChanged: (newScore: Int) -> Unit,
    private var layoutDirection: LayoutDirection
) : Modifier.Node(), PointerInputModifierNode {

    private var isPressed = false
    private var activePointerId: PointerId? = null
    private var lastEmittedScore: Int? = null

    fun update(onScoreChanged: (newScore: Int) -> Unit, layoutDirection: LayoutDirection) {
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
                activeChange?.let { releaseChange ->
                    updateScore(releaseChange.position, bounds)?.let { score ->
                        releaseChange.consume()
                        emitScoreIfChanged(score)
                    }
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
        if (lastEmittedScore == score) return
        lastEmittedScore = score
        onScoreChanged(score)
    }
}

/** Max editable score value for stars input. */
private const val MAX_SCORE_POINTS: Int = 10

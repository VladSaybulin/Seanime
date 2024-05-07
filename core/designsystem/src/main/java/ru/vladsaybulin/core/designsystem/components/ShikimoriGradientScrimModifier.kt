package ru.vladsaybulin.core.designsystem.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import kotlin.math.pow

fun Modifier.drawBackgroundGradientScrim(
    bottomColor: Color,
    topColor: Color = bottomColor.copy(alpha = .0f),
    decay: Float = 3.0f,
    numStops: Int = 16,
) = drawGradientScrim(
    bottomColor = bottomColor,
    topColor = topColor,
    decay = decay,
    numStops = numStops,
    foreground = false
)

fun Modifier.drawForegroundGradientScrim(
    bottomColor: Color,
    topColor: Color = bottomColor.copy(alpha = .0f),
    decay: Float = 3.0f,
    numStops: Int = 16,
): Modifier = drawGradientScrim(
    bottomColor = bottomColor,
    topColor = topColor,
    decay = decay,
    numStops = numStops,
    foreground = true
)

fun Modifier.drawGradientScrim(
    bottomColor: Color,
    topColor: Color,
    decay: Float,
    numStops: Int,
    foreground: Boolean,
): Modifier {
    val delta = 1f / (numStops - 1)
    val colors = List(numStops) { i ->
        val x = delta * i
        val opacity = x.pow(decay)
        lerp(topColor, bottomColor, opacity)
    }
    return this then DrawGradientModifierElement(colors, foreground)
}

private data class DrawGradientModifierElement(
    val colors: List<Color>,
    val foreground: Boolean
) : ModifierNodeElement<GradientModifierNode>() {
    override fun create(): GradientModifierNode = GradientModifierNode(colors, foreground)

    override fun update(node: GradientModifierNode) {
        node.colors = colors
        node.foreground = foreground
    }

    override fun InspectorInfo.inspectableProperties() {
        properties["colors"] = colors
        properties["foreground"] = foreground
    }
}

class GradientModifierNode(
    var colors: List<Color>,
    var foreground: Boolean
) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        if (foreground) {
            drawContent()
        }

        drawRect(brush = Brush.verticalGradient(colors))

        if (!foreground) {
            drawContent()
        }
    }
}


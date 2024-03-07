package ru.vladsaybulin.core.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import kotlin.math.pow


private data class GradientScrimModifierElement(val colors: List<Color>) :
    ModifierNodeElement<GradientModifierNode>() {
    override fun create(): GradientModifierNode = GradientModifierNode(colors)

    override fun update(node: GradientModifierNode) {
        node.colors = colors
    }
}

class GradientModifierNode(var colors: List<Color>) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        drawContent()
        drawRect(brush = Brush.verticalGradient(colors))
    }
}

fun Modifier.drawForegroundGradientScrim(
    targetColor: Color,
    startColor: Color = Color.Transparent,
    decay: Float = 3.0f,
    numStops: Int = 16,
): Modifier {
    val delta = 1f / (numStops - 1)
    val colors = List(numStops) { i ->
        val x = delta * i
        val opacity = x.pow(decay)
        lerp(startColor, targetColor, opacity)
    }
    return this then GradientScrimModifierElement(colors)
}
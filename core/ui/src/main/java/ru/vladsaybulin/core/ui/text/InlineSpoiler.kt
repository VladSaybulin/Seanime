package ru.vladsaybulin.core.ui.text

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import ru.vladsaybulin.model.annotatedtext.SeanimeText

internal class InlineSpoiler(
    override val range: SeanimeText.Range<Unit>,
    initialColor: Color
) : ClickableRange<Unit> {

    var color: Color = initialColor

    private var path: Path? = null

    private val animatedPathOpacity = Animatable(1f)

    suspend fun changeVisible() {
        val targetBoxVisible = animatedPathOpacity.targetValue == 1f
        animatedPathOpacity.animateTo(
            targetValue = if (targetBoxVisible) 0f else 1f,
            animationSpec = tween()
        )
    }

    fun updatePath(textLayout: TextLayoutResult) {
        path = textLayout.getPathForRange(range.start, range.end)
    }

    fun draw(drawScope: DrawScope) {
        if (animatedPathOpacity.value == 0f){
            return
        }

        val nonNullPath = path ?: return

        drawScope.drawPath(
            path = nonNullPath,
            brush = SolidColor(color.copy(alpha = animatedPathOpacity.value))
        )
    }
}
/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.core.ui.text

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.text.TextLayoutResult
import kotlinx.coroutines.launch
import ru.vladsaybulin.model.annotatedtext.SeanimeText

internal fun Modifier.inlineSpoilers(
    textLayout: TextLayoutResult,
    inlineSpoilers: List<SeanimeText.Range<Unit>>,
    inlineSpoilerColor: Color
) = then(
    ClickableInlineSpoilersElement(
        textLayout = textLayout,
        inlineSpoilers = inlineSpoilers,
        inlineSpoilerColor = inlineSpoilerColor
    )
)

private data class ClickableInlineSpoilersElement(
    private val textLayout: TextLayoutResult,
    private val inlineSpoilers: List<SeanimeText.Range<Unit>>,
    private val inlineSpoilerColor: Color
) : ModifierNodeElement<InlineSpoilersModifierNode>() {

    override fun create(): InlineSpoilersModifierNode =
        InlineSpoilersModifierNode(
            textLayout = textLayout,
            inlineSpoilers = inlineSpoilers,
            inlineSpoilerColor = inlineSpoilerColor
        )

    override fun update(node: InlineSpoilersModifierNode) {
        node.update(
            textLayout = textLayout,
            inlineSpoilers = inlineSpoilers,
            inlineSpoilerColor = inlineSpoilerColor
        )
    }
}

private class InlineSpoilersModifierNode(
    private var textLayout: TextLayoutResult,
    private var inlineSpoilers: List<SeanimeText.Range<Unit>>,
    private var inlineSpoilerColor: Color
) : ClickableRangesModifierNode<InlineSpoiler>(
    layout = textLayout,
    clickableItems = emptyList(),
    onItemClick = {}
), DrawModifierNode {

    private var spoilers = inlineSpoilers.map {
        InlineSpoiler(range = it, initialColor = inlineSpoilerColor).apply {
            updatePath(textLayout)
        }
    }

    init {
        super.update(
            layout = textLayout,
            clickableItems = spoilers,
            onItemClick = {
                coroutineScope.launch {
                    it.changeVisible()
                }
            }
        )
    }

    fun update(
        textLayout: TextLayoutResult,
        inlineSpoilers: List<SeanimeText.Range<Unit>>,
        inlineSpoilerColor: Color
    ) {
        val layoutChanged = textLayout != this.textLayout
        val spoilersChanged = inlineSpoilers != this.inlineSpoilers
        val colorChanged = inlineSpoilerColor != this.inlineSpoilerColor

        if (!layoutChanged && !spoilersChanged && !colorChanged) return

        onCancelPointerInput()

        this.textLayout = textLayout
        this.inlineSpoilers = inlineSpoilers
        this.inlineSpoilerColor = inlineSpoilerColor

        if (spoilersChanged) {
            spoilers = inlineSpoilers.map {
                InlineSpoiler(range = it, initialColor = inlineSpoilerColor)
            }
        }

        super.update(
            layout = textLayout,
            clickableItems = spoilers,
            onItemClick = {
                coroutineScope.launch {
                    it.changeVisible()
                }
            }
        )

        if (spoilersChanged || layoutChanged) {
            spoilers.forEach { it.updatePath(textLayout) }
        }

        invalidateDraw()
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        spoilers.forEach { it.draw(this) }
    }
}
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.node.PointerInputModifierNode
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastFilter

internal open class ClickableRangesModifierNode<T : ClickableRange<*>>(
    private var layout: TextLayoutResult,
    private var clickableItems: List<T>,
    private var onItemClick: (item: T) -> Unit
) : Modifier.Node(), PointerInputModifierNode {

    private var pressedItem: T? = null

    fun update(
        layout: TextLayoutResult,
        clickableItems: List<T>,
        onItemClick: (item: T) -> Unit
    ) {
        if (this.layout == layout && this.clickableItems == clickableItems && this.onItemClick == onItemClick) {
            return
        }

        onCancelPointerInput()

        this.layout = layout
        this.clickableItems = clickableItems
        this.onItemClick = onItemClick
    }

    private fun onPress(position: Offset): Boolean {
        pressedItem = getIntersectItem(getTextOffsetForPosition(position))
        return pressedItem != null
    }

    private fun onMove(position: Offset): Boolean {
        if (pressedItem == null) return false

        val movedOnItem = getIntersectItem(getTextOffsetForPosition(position))

        if (movedOnItem == null || pressedItem!!.range != movedOnItem.range) {
            pressedItem = null
            return false
        }

        return true
    }

    private fun onRelease(position: Offset): Boolean {
        val nonNullPressedItem = pressedItem ?: return false

        val releasedOnItem = getIntersectItem(getTextOffsetForPosition(position))

        pressedItem = null

        if (releasedOnItem == null || nonNullPressedItem.range != releasedOnItem.range) {
            return false
        }

        onItemClick(nonNullPressedItem)
        return true
    }

    private fun getIntersectItem(textOffset: Int): T? =
        clickableItems.fastFilter {
            intersect(
                textOffset,
                textOffset,
                it.range.start,
                it.range.end
            )
        }.firstOrNull()

    private fun getTextOffsetForPosition(position: Offset) = layout.getOffsetForPosition(position)

    override fun onCancelPointerInput() {
        pressedItem = null
    }

    override fun onPointerEvent(
        pointerEvent: PointerEvent,
        pass: PointerEventPass,
        bounds: IntSize
    ) {
        val change = pointerEvent.changes.first()

        val consumed = when (pointerEvent.type) {
            PointerEventType.Press -> onPress(change.position)
            PointerEventType.Move -> onMove(change.position)
            PointerEventType.Release -> onRelease(change.position)
            else -> false
        }

        if (consumed) {
            change.consume()
        }
    }
}
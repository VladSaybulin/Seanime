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
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.text.TextLayoutResult
import ru.vladsaybulin.model.annotatedtext.SeanimeText

internal fun Modifier.clickableLinks(
    textLayout: TextLayoutResult,
    links: List<SeanimeText.Range<String>>,
    onClick: (tag: String, annotation: String) -> Unit
) = then(SeanimeTextClickableLinksElement(textLayout, links, onClick))

private data class SeanimeTextClickableLinksElement(
    val textLayout: TextLayoutResult,
    val links: List<SeanimeText.Range<String>>,
    val onClick: (tag: String, annotation: String) -> Unit
): ModifierNodeElement<ClickableRangesModifierNode<RangeWrapper<String>>>() {

    val linksRangeWrappers = links.map { RangeWrapper(it) }

    override fun create(): ClickableRangesModifierNode<RangeWrapper<String>> =
        ClickableRangesModifierNode(
            layout = textLayout,
            clickableItems = linksRangeWrappers,
            onItemClick = { onClick(it.tag, it.item) }
        )

    override fun update(node: ClickableRangesModifierNode<RangeWrapper<String>>) {
       node.update(
           layout = textLayout,
           clickableItems = linksRangeWrappers,
           onItemClick = { onClick(it.tag, it.item) }
       )
    }

}

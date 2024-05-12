package ru.vladsaybulin.core.ui.annotatedtext

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

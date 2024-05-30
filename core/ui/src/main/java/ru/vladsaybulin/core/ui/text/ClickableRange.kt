package ru.vladsaybulin.core.ui.text

import ru.vladsaybulin.model.annotatedtext.SeanimeText

interface ClickableRange <Item> {
    val range: SeanimeText.Range<Item>
}

class RangeWrapper <Item>(
    override val range: SeanimeText.Range<Item>
) : ClickableRange<Item> {
    val start: Int = range.start
    val end: Int = range.end
    val tag: String = range.tag
    val item: Item = range.item
}
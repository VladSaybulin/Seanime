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

package ru.vladsaybulin.core.textprocessor.html

import kotlinx.collections.immutable.toImmutableList
import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.annotatedtext.SeanimeText.Range

class SeanimeTextBuilder : Appendable, CharSequence {

    private data class MutableRange <T>(
        val start: Int,
        val tag: String = "",
        val item: T,
        var end: Int = Int.MIN_VALUE,
    ) {
        fun toTextRange(defaultEnd: Int): Range<T> {
            val end = if (end == Int.MIN_VALUE) defaultEnd else end
            return Range(
                start = start,
                end = end,
                tag = tag,
                item = item
            )
        }
    }

    private val text = StringBuilder()
    private val styles: MutableList<MutableRange<SeanimeText.Style>> = mutableListOf()
    private val spoilerBlocks: MutableList<MutableRange<SeanimeText.SpoilerBlockItem>> = mutableListOf()
    private val inlineSpoilers: MutableList<MutableRange<Unit>> = mutableListOf()
    private val links: MutableList<MutableRange<String>> = mutableListOf()

    private val stack: MutableList<MutableRange<*>> = mutableListOf()

    override val length: Int
        get() = text.length

    override fun get(index: Int): Char =
        text[index]

    override fun subSequence(startIndex: Int, endIndex: Int) =
        text.subSequence(startIndex, endIndex)

    fun toSeanimeText(): SeanimeText = SeanimeText(
        text = text.toString(),
        styles = styles.map { it.toTextRange(text.length) }.toImmutableList(),
        spoilerBlocks = spoilerBlocks.map { it.toTextRange(text.length) }.toImmutableList(),
        inlineSpoilers = inlineSpoilers.map { it.toTextRange(text.length) }.toImmutableList(),
        links = links.map { it.toTextRange(text.length) }.toImmutableList()
    )

    fun append(seanimeText: SeanimeText) {
        val offset = text.length
        append(seanimeText.text)

        for (style in seanimeText.styles) {
            addStyle(
                style = style.item,
                start = style.start + offset,
                end = style.end + offset
            )
        }
        for (inlineSpoiler in seanimeText.inlineSpoilers) {
            addInlineSpoiler(
                start = inlineSpoiler.start + offset,
                end = inlineSpoiler.end + offset
            )
        }
        for (spoilerBlock in seanimeText.spoilerBlocks) {
            addSpoilerBlock(
                spoilerBlockItem = spoilerBlock.item,
                start = spoilerBlock.start + offset,
                end = spoilerBlock.end + offset
            )
        }
        for (link in seanimeText.links) {
            addLink(
                tag = link.tag,
                annotation = link.item,
                start = link.start + offset,
                end = link.end + offset
            )
        }
    }

    override fun append(csq: CharSequence?): java.lang.Appendable {
        text.append(csq)
        return this
    }

    override fun append(csq: CharSequence?, start: Int, end: Int): java.lang.Appendable {
        text.append(csq, start, end)
        return this
    }

    override fun append(c: Char): java.lang.Appendable {
        text.append(c)
        return this
    }

    fun addLink(tag: String, annotation: String, start: Int, end: Int) {
        links.add(MutableRange(start = start, tag = tag, item = annotation, end = end))
    }

    fun addSpoilerBlock(spoilerBlockItem: SeanimeText.SpoilerBlockItem, start: Int, end: Int) {
        spoilerBlocks.add(MutableRange(start = start, end = end, item = spoilerBlockItem))
    }

    fun addInlineSpoiler(start: Int, end: Int) {
        inlineSpoilers.add(MutableRange(start = start, end = end, item = Unit))
    }

    fun addStyle(style: SeanimeText.Style, start: Int, end: Int) {
        styles.add(MutableRange(start = start, end = end, item = style))
    }

    fun pushLink(tag: String, annotation: String): Int {
        MutableRange(start = text.length, tag = tag, item = annotation).apply {
            links.add(this)
            stack.add(this)
        }
        return stack.size - 1
    }

    fun pushSpoilerBlock(spoilerBlockItem: SeanimeText.SpoilerBlockItem): Int {
        MutableRange(start = text.length, item = spoilerBlockItem).apply {
            spoilerBlocks.add(this)
            stack.add(this)
        }
        return stack.size - 1
    }

    fun pushInlineSpoiler(): Int {
        MutableRange(start = text.length, item = Unit).apply {
            inlineSpoilers.add(this)
            stack.add(this)
        }
        return stack.size - 1
    }

    fun pushStyle(style: SeanimeText.Style): Int {
        MutableRange(start = text.length, item = style).apply {
            styles.add(this)
            stack.add(this)
        }
        return stack.size - 1
    }

    fun pop() {
        check(stack.isNotEmpty())
        stack.removeLast().run {
            end = text.length
        }
    }

    fun pop(index: Int) {
        check(stack.size > index) {
            "$index of ${stack.size}"
        }
        while (stack.lastIndex >= index) {
            pop()
        }
    }
}

fun SeanimeTextBuilder.link(
    tag: String,
    annotation: String,
    block: SeanimeTextBuilder.() -> Unit
) {
    val index = pushLink(tag, annotation)
    block()
    pop(index)
}

fun SeanimeTextBuilder.withStyle(
    style: SeanimeText.Style,
    block: SeanimeTextBuilder.() -> Unit
) {
    val index = pushStyle(style)
    block()
    pop(index)
}

fun SeanimeTextBuilder.inlineSpoilers(
    block: SeanimeTextBuilder.() -> Unit
) {
    val index = pushInlineSpoiler()
    block()
    pop(index)
}

fun SeanimeTextBuilder.spoilerBlock(
    item: SeanimeText.SpoilerBlockItem,
    block: SeanimeTextBuilder.() -> Unit
) {
    val index = pushSpoilerBlock(item)
    block()
    pop(index)
}
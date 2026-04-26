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

import androidx.compose.runtime.Immutable
import androidx.compose.ui.util.fastFilter
import androidx.compose.ui.util.fastMinByOrNull
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ru.vladsaybulin.model.annotatedtext.SeanimeText

@Immutable
internal sealed class SeanimeTextPart {

    @Immutable
    data class Regular(val text: SeanimeText) : SeanimeTextPart()

    @Immutable
    data class SpoilerBlock(val title: SeanimeText?, val parts: List<SeanimeTextPart>) :
        SeanimeTextPart()

}

internal fun SeanimeText.splitToParts(): List<SeanimeTextPart> = buildList {
    var start = 0

    while (true) {
        val spoilerBlockRange = firstSpoilerBlock(
            start = start,
            end = length,
            item = SeanimeText.SpoilerBlockItem.Block
        ) ?: break

        val spoilerTitleRange = firstSpoilerBlock(
            start = spoilerBlockRange.start,
            end = spoilerBlockRange.start,
            item = SeanimeText.SpoilerBlockItem.Title
        )

        val beforeSpoilerText = subSequence(start, spoilerBlockRange.start)
        add(SeanimeTextPart.Regular(beforeSpoilerText))

        val spoilerTitle = spoilerTitleRange?.let { subSequence(it.start, it.end) }

        val spoilerText = spoilerText(spoilerBlockRange, spoilerTitleRange)

        add(SeanimeTextPart.SpoilerBlock(spoilerTitle, spoilerText.splitToParts()))

        start = spoilerBlockRange.end
    }

    val tail = subSequence(start, length)
    add(SeanimeTextPart.Regular(tail))
}

private fun SeanimeText.subSequence(startIndex: Int, endIndex: Int): SeanimeText {
    val text = text.substring(startIndex, endIndex)
    return SeanimeText(
        text = text,
        styles = filterRanges(styles, startIndex, endIndex),
        inlineSpoilers = filterRanges(inlineSpoilers, startIndex, endIndex),
        spoilerBlocks = filterRanges(spoilerBlocks, startIndex, endIndex),
        links = filterRanges(links, startIndex, endIndex),
    )
}

private fun SeanimeText.spoilerText(
    spoilerRange: SeanimeText.Range<SeanimeText.SpoilerBlockItem>,
    titleRange: SeanimeText.Range<SeanimeText.SpoilerBlockItem>?,
): SeanimeText {
    val startIndex = titleRange?.end ?: spoilerRange.start
    val endIndex = spoilerRange.end
    val text = text.substring(startIndex, endIndex)
    val filteredSpoilerBlocks = spoilerBlocks.filter { it != spoilerRange && it != titleRange }
    return SeanimeText(
        text = text,
        styles = filterRanges(styles, startIndex, endIndex),
        inlineSpoilers = filterRanges(inlineSpoilers, startIndex, endIndex),
        spoilerBlocks = filterRanges(filteredSpoilerBlocks, startIndex, endIndex),
        links = filterRanges(links, startIndex, endIndex),
    )
}

private fun SeanimeText.firstSpoilerBlock(
    start: Int,
    end: Int,
    item: SeanimeText.SpoilerBlockItem,
): SeanimeText.Range<SeanimeText.SpoilerBlockItem>? =
    spoilerBlocks.fastFilter { it.item == item && intersect(start, end, it.start, it.end) }
        .fastMinByOrNull { it.start }

private fun <T> filterRanges(
    ranges: List<SeanimeText.Range<out T>>,
    start: Int,
    end: Int,
): ImmutableList<SeanimeText.Range<T>> {
    require(start <= end) { "start ($start) should be less than or equal to end ($end)" }

    return ranges
        .asSequence()
        .filter { intersect(start, end, it.start, it.end) }
        .map {
            SeanimeText.Range(
                item = it.item,
                start = maxOf(start, it.start) - start,
                end = minOf(end, it.end) - start,
                tag = it.tag
            )
        }
        .toImmutableList()
}
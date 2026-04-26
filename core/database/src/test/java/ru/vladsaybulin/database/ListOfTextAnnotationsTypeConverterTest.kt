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

package ru.vladsaybulin.database

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.vladsaybulin.database.models.text.ProtoReadyStyleValue
import ru.vladsaybulin.database.models.text.ProtoSeanimeTextRange
import ru.vladsaybulin.database.models.text.ProtoSeanimeTextSpoilerBlockItem
import ru.vladsaybulin.database.models.text.ProtoSeanimeTextStyle
import ru.vladsaybulin.database.utils.TextRangesTypeConverter

class ListOfTextAnnotationsTypeConverterTest {

    private lateinit var typeConverter: TextRangesTypeConverter

    @OptIn(ExperimentalSerializationApi::class)
    @Before
    fun setup() {
        typeConverter = TextRangesTypeConverter(ProtoBuf)
    }

    @Test
    fun textRangesTypeConverter_match_links_with_serialized_and_then_deserialized_links() {
        val links = listOf(
            ProtoSeanimeTextRange(start = 1, end = 5, tag = "url", item = "https://wikiperdia.com"),
            ProtoSeanimeTextRange(start = 10, end = 18, tag = "anime", item = "20")
        )
        val deserializedLinks = typeConverter.run {
            stringToLinks(linksToString(links))
        }

        assertEquals(links, deserializedLinks)
    }

    @Test
    fun textRangesTypeConverter_match_styles_with_serialized_and_then_deserialized_styles() {
        val styles = listOf<ProtoSeanimeTextRange<ProtoSeanimeTextStyle>>(
            ProtoSeanimeTextRange(start = 1, end = 5, tag = "", item = ProtoSeanimeTextStyle.ReadyStyle(ProtoReadyStyleValue.BOLD)),
            ProtoSeanimeTextRange(start = 10, end = 18, tag = "", item = ProtoSeanimeTextStyle.ReadyStyle(ProtoReadyStyleValue.ITALIC)),
            ProtoSeanimeTextRange(start = 21, end = 29, tag = "", item = ProtoSeanimeTextStyle.ReadyStyle(ProtoReadyStyleValue.UNDERLINE)),
            ProtoSeanimeTextRange(start = 32, end = 56, tag = "", item = ProtoSeanimeTextStyle.ReadyStyle(ProtoReadyStyleValue.STRIKETHROUGH)),
            ProtoSeanimeTextRange(start = 43, end = 49, tag = "", item = ProtoSeanimeTextStyle.ReadyStyle(ProtoReadyStyleValue.H1))
        )
        val deserializedStyles = typeConverter.run {
            stringToStyles(stylesToString(styles))
        }

        assertEquals(styles, deserializedStyles)
    }

    @Test
    fun textRangesTypeConverter_match_spoiler_blocks_with_serialized_and_then_deserialized_spoiler_blocks() {
        val spoilerBlocks = listOf(
            ProtoSeanimeTextRange(start = 1, end = 28, tag = "", item = ProtoSeanimeTextSpoilerBlockItem.Block),
            ProtoSeanimeTextRange(start = 1, end = 5, tag = "", item = ProtoSeanimeTextSpoilerBlockItem.Title),
            ProtoSeanimeTextRange(start = 44, end = 125, tag = "", item = ProtoSeanimeTextSpoilerBlockItem.Block)
        )
        val deserializedSpoilerBlocks = typeConverter.run {
            stringToSpoilerBlocks(spoilerBlocksToString(spoilerBlocks))
        }

        assertEquals(spoilerBlocks, deserializedSpoilerBlocks)
    }

    @Test
    fun textRangesTypeConverter_match_inline_spoilers_with_serialized_and_then_deserialized_inline_spoilers() {
        val inlineSpoilers = listOf(
            ProtoSeanimeTextRange(start = 1, end = 28, tag = "", item = Unit),
            ProtoSeanimeTextRange(start = 1, end = 5, tag = "", item = Unit),
            ProtoSeanimeTextRange(start = 44, end = 125, tag = "", item = Unit)
        )
        val deserializedInlineSpoilers = typeConverter.run {
            stringToInlineSpoilers(inlineSpoilersToString(inlineSpoilers))
        }

        assertEquals(inlineSpoilers, deserializedInlineSpoilers)
    }
}
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

package ru.vladsaybulin.model.annotatedtext

import kotlinx.collections.immutable.ImmutableList

data class SeanimeText(
    val text: String,
    val styles: ImmutableList<Range<Style>>,
    val inlineSpoilers: ImmutableList<Range<Unit>>,
    val spoilerBlocks: ImmutableList<Range<SpoilerBlockItem>>,
    val links: ImmutableList<Range<String>>
) {

    data class Range<T>(
        val start: Int,
        val end: Int,
        val tag: String,
        val item: T
    )

    enum class SpoilerBlockItem {
        Block, Title
    }

    enum class ReadyStyleValue {
        H1,
        H2,
        H3,
        H4,
        H5,
        H6,
        Underline,
        Strikethrough,
        Bold,
        Italic
    }

    sealed class Style {
        data class ReadyStyle(val value: ReadyStyleValue) : Style()
    }

    val length: Int = text.length

}
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
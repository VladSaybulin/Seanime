package ru.vladsaybulin.core.textprocessor.html2bbcodes

import ru.vladsaybulin.core.textprocessor.TagTransformer

internal val HeaderTransformer = TagTransformer<StringBuilder> {
    builder.buildBBCodeTag(name = tagNode.name, body = ::transformChildren)
}

internal val BoldTransformer = TagTransformer<StringBuilder> {
    builder.buildBBCodeTag(name = "b", body = ::transformChildren)
}

internal val ItalicTransformer = TagTransformer<StringBuilder> {
    builder.buildBBCodeTag(name = "i", body = ::transformChildren)
}

internal val UnderlineTransformer = TagTransformer<StringBuilder> {
    builder.buildBBCodeTag(name = "u", body = ::transformChildren)
}

internal val StrikethroughTransformer = TagTransformer<StringBuilder> {
    builder.buildBBCodeTag(name = "s", body = ::transformChildren)
}
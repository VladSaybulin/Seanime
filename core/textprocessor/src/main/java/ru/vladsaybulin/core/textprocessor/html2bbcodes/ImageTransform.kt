package ru.vladsaybulin.core.textprocessor.html2bbcodes

import ru.vladsaybulin.core.textprocessor.TagTransformer

internal val ImageTransform = TagTransformer<StringBuilder> {
    val src = tagNode.attributes["src"]
    if (src != null) {
        builder.buildBBCodeTag(
            name = "img",
            body = { builder.append(src) }
        )
    } else proceed()
}
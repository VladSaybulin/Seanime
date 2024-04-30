package ru.vladsaybulin.core.textprocessor.html2bbcodes

import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.util.rawBody

internal val CodeTransformer = TagTransformer<StringBuilder> {
    builder.buildBBCodeTag(
        name = "code",
        body = {
            builder.append(tagNode.rawBody)
        }
    )
}
package ru.vladsaybulin.core.textprocessor.html2bbcodes

import ru.vladsaybulin.core.textprocessor.TagTransformer

internal val ColorTransformer = TagTransformer<StringBuilder> {
    val styleAttribute = tagNode.attributes["style"]
    if (styleAttribute != null) {
        val matches = Regex("color.*:.*(#.+)").find(styleAttribute)
        if (matches != null) {
            builder.buildBBCodeTag(
                name = "color",
                attribute = matches.groupValues.first(),
                body = {
                    //Proceed for other span style
                    proceed()
                }
            )
        }
    } else {
        proceed()
    }
}
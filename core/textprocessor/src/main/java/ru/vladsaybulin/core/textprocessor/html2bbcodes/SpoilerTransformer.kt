package ru.vladsaybulin.core.textprocessor.html2bbcodes

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.util.rawBody

internal val SpoilerBlockTransformer = TagTransformer<StringBuilder> {
    val tagClasses = tagNode.attributes["class"]?.split(' ')

    if (tagClasses == null || !tagClasses.contains("b-spoiler_block")) {
        proceed()
        return@TagTransformer
    }

    var title: String? = null

    val newChildren = tagNode.children.toMutableList()
    val newChildrenIterator = newChildren.listIterator()

    while (newChildrenIterator.hasNext()) {
        val node = newChildrenIterator.next()

        if (node !is TagNode) continue
        if (node.name != "span") continue
        val tabIndex = node.attributes["tabindex"] ?: continue

        if (tabIndex == "0") {
            title = node.rawBody.takeIf { it != "спойлер" }

            newChildrenIterator.remove()
            break
        }
    }

    builder.buildBBCodeTag(
        name = "spoiler",
        attribute = title,
        body = { transformChildren(newChildren) }
    )
}

internal val SpoilerInlineTransformer = TagTransformer<StringBuilder> {
    val tagClasses = tagNode.attributes["class"]?.split(' ')

    if (tagClasses == null || !tagClasses.contains("b-spoiler_inline")) {
        proceed()
        return@TagTransformer
    }

    builder.append("||")
    builder.append(tagNode.rawString.substring(tagNode.bodyBegin, tagNode.bodyEnd))
    builder.append("||")
}
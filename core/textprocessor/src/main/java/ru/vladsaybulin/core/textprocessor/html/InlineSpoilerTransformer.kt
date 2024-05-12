package ru.vladsaybulin.core.textprocessor.html

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope
import ru.vladsaybulin.core.textprocessor.util.containsHtmlClass

object InlineSpoilerTransformer : TagTransformer<SeanimeTextBuilder> {
    override val tagNames: Set<String> = setOf("span")

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        if (!tagNode.containsHtmlClass("b-spoiler_inline")) {
            return TagTransformerResult.NotTransformed
        }
        builder.inlineSpoilers {
            transformChildren()
        }
        return TagTransformerResult.Success
    }
}
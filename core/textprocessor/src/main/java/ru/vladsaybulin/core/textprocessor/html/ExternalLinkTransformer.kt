package ru.vladsaybulin.core.textprocessor.html

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope

object ExternalLinkTransformer : TagTransformer<SeanimeTextBuilder> {

    override val tagNames: Set<String> = setOf("a")

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        val href = tagNode.attributes["href"] ?: return TagTransformerResult.NotTransformed

        builder.link(tag = "url", annotation = href) {
            transformChildren()
        }
        return TagTransformerResult.Success
    }
}
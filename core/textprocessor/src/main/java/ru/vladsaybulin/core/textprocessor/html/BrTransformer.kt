package ru.vladsaybulin.core.textprocessor.html

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope

object BrTransformer : TagTransformer<SeanimeTextBuilder> {

    override val tagNames: Set<String> = setOf("br")

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        if (builder.last() != '\n') {
            builder.append("\n")
        }
        return TagTransformerResult.Success
    }
}
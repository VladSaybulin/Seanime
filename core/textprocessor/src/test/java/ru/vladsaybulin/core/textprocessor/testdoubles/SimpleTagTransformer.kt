package ru.vladsaybulin.core.textprocessor.testdoubles

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope

class SimpleTagTransformer(
    override val priority: Int = 0
) : TagTransformer<StringBuilder> {
    override val tagNames: Set<String> = setOf("simple")

    override fun TagTransformerScope<StringBuilder>.transform(
        tagNode: TagNode,
        builder: StringBuilder
    ): TagTransformerResult {
        builder.append("[")
        transformChildren()
        builder.append("]")
        return TagTransformerResult.Success
    }
}
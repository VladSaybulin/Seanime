package ru.vladsaybulin.core.textprocessor.html

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope
import ru.vladsaybulin.core.textprocessor.util.htmlClasses

/**
 * Filter tags with name-en classes
 */
object LocalizedNameTransformer : TagTransformer<SeanimeTextBuilder> {

    override val tagNames: Set<String> = setOf("span")

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        if (tagNode.htmlClasses()?.contains("name-ru") != true) {
            return TagTransformerResult.NotTransformed
        }
        transformChildren()
        return TagTransformerResult.Success
    }
}
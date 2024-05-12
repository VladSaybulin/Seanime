package ru.vladsaybulin.core.textprocessor.testdoubles

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope

class BuilderProviderTagTransformer : TagTransformer<StringBuilder> {
    override val tagNames: Set<String> = setOf("builder_provider")

    override fun TagTransformerScope<StringBuilder>.transform(
        tagNode: TagNode,
        builder: StringBuilder
    ): TagTransformerResult {
        val deferredBuilder = StringBuilder()
        transformChildren(textNodeBuilder = builder) {
            when (it.name) {
                "append_me" -> builder
                "defer_me" -> deferredBuilder
                else -> null
            }
        }
        builder.append(deferredBuilder.toString())

        return TagTransformerResult.Success
    }
}
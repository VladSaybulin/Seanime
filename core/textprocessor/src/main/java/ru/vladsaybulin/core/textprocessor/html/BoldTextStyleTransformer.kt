package ru.vladsaybulin.core.textprocessor.html

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue
import ru.vladsaybulin.model.annotatedtext.SeanimeText.Style

object BoldTextStyleTransformer : TagTransformer<SeanimeTextBuilder> {

    override val tagNames: Set<String> = setOf("b", "strong")

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        builder.withStyle(Style.ReadyStyle(ReadyStyleValue.Bold)) {
            transformChildren()
        }
        return TagTransformerResult.Success
    }
}
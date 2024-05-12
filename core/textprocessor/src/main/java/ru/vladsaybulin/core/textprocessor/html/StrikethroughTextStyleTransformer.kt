package ru.vladsaybulin.core.textprocessor.html

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope
import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue

object StrikethroughTextStyleTransformer : TagTransformer<SeanimeTextBuilder> {

    override val tagNames: Set<String> = setOf("del")

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        builder.withStyle(
            style = SeanimeText.Style.ReadyStyle(ReadyStyleValue.Strikethrough)
        ) {
            transformChildren()
        }
        return TagTransformerResult.Success
    }
}
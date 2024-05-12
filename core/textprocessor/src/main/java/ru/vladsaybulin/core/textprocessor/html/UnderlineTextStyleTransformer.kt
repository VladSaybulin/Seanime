package ru.vladsaybulin.core.textprocessor.html

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope
import ru.vladsaybulin.model.annotatedtext.SeanimeText

object UnderlineTextStyleTransformer : TagTransformer<SeanimeTextBuilder> {

    override val tagNames: Set<String> = setOf("u", "ins")

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        builder.withStyle(
            style = SeanimeText.Style.ReadyStyle(SeanimeText.ReadyStyleValue.Underline)
        ) {
            transformChildren()
        }
        return TagTransformerResult.Success
    }

}
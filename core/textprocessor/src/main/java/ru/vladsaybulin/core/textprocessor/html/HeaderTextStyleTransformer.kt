package ru.vladsaybulin.core.textprocessor.html

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope
import ru.vladsaybulin.model.annotatedtext.SeanimeText

object HeaderTextStyleTransformer : TagTransformer<SeanimeTextBuilder> {

    override val tagNames: Set<String> = setOf("h1", "h2", "h3", "h4", "h5", "h6")

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        val readyStyleValue = when (tagNode.name) {
            "h1" -> SeanimeText.ReadyStyleValue.H1
            "h2" -> SeanimeText.ReadyStyleValue.H2
            "h3" -> SeanimeText.ReadyStyleValue.H3
            "h4" -> SeanimeText.ReadyStyleValue.H4
            "h5" -> SeanimeText.ReadyStyleValue.H5
            "h6" -> SeanimeText.ReadyStyleValue.H6
            else -> return TagTransformerResult.NotTransformed
        }

        builder.withStyle(SeanimeText.Style.ReadyStyle(readyStyleValue)) {
            if (get(length - 1) != '\n') {
                append("\n")
            }
            transformChildren()
            append("\n")
        }
        return TagTransformerResult.Success
    }
}
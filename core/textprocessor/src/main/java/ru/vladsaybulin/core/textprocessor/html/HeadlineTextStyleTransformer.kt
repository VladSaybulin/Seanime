package ru.vladsaybulin.core.textprocessor.html

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope
import ru.vladsaybulin.core.textprocessor.util.htmlClasses
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue
import ru.vladsaybulin.model.annotatedtext.SeanimeText.Style

object HeadlineTextStyleTransformer : TagTransformer<SeanimeTextBuilder> {

    override val tagNames: Set<String> = setOf("span")

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        val classes = tagNode.htmlClasses()?.ifEmpty { null }
            ?: return TagTransformerResult.NotTransformed

        val readyStyleValue = when {
            classes.contains("headline") -> ReadyStyleValue.H5
            classes.contains("midheadline") -> ReadyStyleValue.H6
            else -> return TagTransformerResult.NotTransformed
        }

        builder.withStyle(Style.ReadyStyle(readyStyleValue)) {
            if (get(length - 1) != '\n') {
                append("\n")
            }
            transformChildren()
            append("\n")
        }
        return TagTransformerResult.Success
    }

}
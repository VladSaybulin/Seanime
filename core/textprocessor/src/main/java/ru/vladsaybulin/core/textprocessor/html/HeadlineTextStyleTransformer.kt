package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.util.htmlClasses

object HeadlineTextStyleTransformer : TagTransformer<AnnotatedTextBuilder> {
    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        val classes = chain.tagNode.htmlClasses() ?: emptyList()
        val style = when {
            classes.contains("headline") -> "h5"
            classes.contains("midheadline") -> "h6"
            else -> {
                chain.proceed()
                return
            }
        }

        chain.builder.withAnnotation(tag = "text_style", annotation = style) {
            chain.transformChildren()
            append("\n")
        }
    }

}
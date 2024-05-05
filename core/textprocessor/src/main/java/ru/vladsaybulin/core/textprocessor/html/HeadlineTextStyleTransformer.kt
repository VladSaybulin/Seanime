package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.util.htmlClasses
import ru.vladsaybulin.model.annotatedtext.AnnotatedText

object HeadlineTextStyleTransformer : TagTransformer<AnnotatedTextBuilder> {
    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        val classes = chain.tagNode.htmlClasses() ?: emptyList()
        val style = when {
            classes.contains("headline") -> AnnotatedText.TextStyle.H5
            classes.contains("midheadline") -> AnnotatedText.TextStyle.H6
            else -> {
                chain.proceed()
                return
            }
        }

        chain.builder.withStyle(style) {
            chain.transformChildren()
            append("\n")
        }
    }

}
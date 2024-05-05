package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.model.annotatedtext.AnnotatedText

object HeaderTextStyleTransformer : TagTransformer<AnnotatedTextBuilder> {

    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        val style = when(chain.tagNode.name) {
            "h1" -> AnnotatedText.TextStyle.H1
            "h2" -> AnnotatedText.TextStyle.H2
            "h3" -> AnnotatedText.TextStyle.H3
            "h4" -> AnnotatedText.TextStyle.H4
            "h5" -> AnnotatedText.TextStyle.H5
            "h6" -> AnnotatedText.TextStyle.H6
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
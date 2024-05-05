package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.model.annotatedtext.AnnotatedText.TextStyle

object ItalicTextStyleTransformer : TagTransformer<AnnotatedTextBuilder> {

    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        when (chain.tagNode.name) {
            "i", "em" -> chain.builder.withStyle(TextStyle.Italic) { chain.transformChildren() }
            else -> chain.proceed()
        }
    }
}
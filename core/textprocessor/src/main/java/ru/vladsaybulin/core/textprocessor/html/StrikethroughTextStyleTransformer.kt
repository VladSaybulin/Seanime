package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.model.annotatedtext.AnnotatedText.TextStyle

object StrikethroughTextStyleTransformer : TagTransformer<AnnotatedTextBuilder> {
    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        when (chain.tagNode.name) {
            "del" -> chain.builder.withStyle(TextStyle.Strikethrough) { chain.transformChildren() }
            else -> chain.proceed()
        }
    }
}
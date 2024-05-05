package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.model.annotatedtext.AnnotatedText.TextStyle

object UnderlineTextStyleTransformer : TagTransformer<AnnotatedTextBuilder> {
    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        when (chain.tagNode.name) {
            "u", "ins" -> chain.builder.withStyle(TextStyle.Underline) { chain.transformChildren() }
            else -> chain.proceed()
        }
    }

}
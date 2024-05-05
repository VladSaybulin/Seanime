package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.model.annotatedtext.AnnotatedText

object BoldTextStyleTransformer : TagTransformer<AnnotatedTextBuilder> {
    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        when (chain.tagNode.name) {
            "strong", "b" -> chain.builder.withStyle(AnnotatedText.TextStyle.Bold) {
                chain.transformChildren()
            }
            else -> chain.proceed()
        }
    }

}
package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer

object UnderlineTextStyleTransformer : TagTransformer<AnnotatedTextBuilder> {
    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        when (chain.tagNode.name) {
            "u", "ins" -> chain.builder.withAnnotation(tag = "text_style", annotation = "u") {
                chain.transformChildren()
            }
            else -> chain.proceed()
        }
    }

}
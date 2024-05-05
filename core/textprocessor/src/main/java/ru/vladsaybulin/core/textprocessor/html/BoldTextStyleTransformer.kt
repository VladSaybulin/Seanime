package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer

object BoldTextStyleTransformer : TagTransformer<AnnotatedTextBuilder> {
    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        when (chain.tagNode.name) {
            "strong", "b" -> chain.builder.withAnnotation(tag = "text_style", annotation = "b") {
                chain.transformChildren()
            }
            else -> chain.proceed()
        }
    }

}
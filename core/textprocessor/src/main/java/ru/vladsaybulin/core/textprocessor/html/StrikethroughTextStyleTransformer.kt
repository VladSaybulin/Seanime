package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer

object StrikethroughTextStyleTransformer : TagTransformer<AnnotatedTextBuilder> {
    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        when (chain.tagNode.name) {
            "del" -> chain.builder.withAnnotation(tag = "text_style", annotation = "s") {
                chain.transformChildren()
            }
            else -> chain.proceed()
        }
    }
}
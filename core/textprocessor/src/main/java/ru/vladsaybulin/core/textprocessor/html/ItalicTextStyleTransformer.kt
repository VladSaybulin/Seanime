package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer

object ItalicTextStyleTransformer : TagTransformer<AnnotatedTextBuilder> {

    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        when (chain.tagNode.name) {
            "i", "em" -> chain.builder.withAnnotation(tag = "text_style", annotation = "i") { chain.transformChildren() }
            else -> chain.proceed()
        }
    }
}
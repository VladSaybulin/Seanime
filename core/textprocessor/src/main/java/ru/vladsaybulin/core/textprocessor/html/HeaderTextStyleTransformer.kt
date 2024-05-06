package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer

object HeaderTextStyleTransformer : TagTransformer<AnnotatedTextBuilder> {

    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        if (chain.tagNode.name !in listOf("h1", "h2", "h3", "h4", "h5", "h6")) {
            chain.proceed()
            return
        }

        chain.builder.withAnnotation(tag = "text_style", annotation = chain.tagNode.name) {
            if (get(length - 1) != '\n') {
                append("\n")
            }
            chain.transformChildren()
            append("\n")
        }
    }
}
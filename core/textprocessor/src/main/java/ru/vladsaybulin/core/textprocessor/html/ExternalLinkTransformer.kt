package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.model.annotatedtext.AnnotatedText

object ExternalLinkTransformer : TagTransformer<AnnotatedTextBuilder> {

    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        val href = chain.tagNode.attributes["href"]

        if (href == null) {
            chain.proceed()
            return
        }

        chain.builder.withStyleAndAnnotation(
            style = AnnotatedText.TextStyle.Link,
            tag = "url",
            annotation = href
        ) {
            chain.transformChildren()
        }
    }
}
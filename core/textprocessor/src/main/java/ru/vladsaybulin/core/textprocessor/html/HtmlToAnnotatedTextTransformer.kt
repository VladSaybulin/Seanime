package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.DocumentTransformer

val HtmlToAnnotatedTextTransformer = DocumentTransformer(
    tagsTransformers = mapOf(
        "a" to listOf(ShikimoriLinkTransformer, ExternalLinkTransformer),
        "span" to listOf(LocalizedNameTransformer),
        "strong" to listOf(BoldTextStyleTransformer),
        "b" to listOf(BoldTextStyleTransformer),
        "em" to listOf(ItalicTextStyleTransformer),
        "u" to listOf(UnderlineTextStyleTransformer),
        "del" to listOf(StrikethroughTextStyleTransformer),
        "div" to listOf(HeadlineTextStyleTransformer),
        "h1" to listOf(HeaderTextStyleTransformer),
        "h2" to listOf(HeaderTextStyleTransformer),
        "h3" to listOf(HeaderTextStyleTransformer),
        "h4" to listOf(HeaderTextStyleTransformer),
        "h5" to listOf(HeaderTextStyleTransformer),
        "h6" to listOf(HeaderTextStyleTransformer)
    )
)
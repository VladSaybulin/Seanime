package ru.vladsaybulin.core.textprocessor.html2bbcodes

import ru.vladsaybulin.core.textprocessor.DocumentTransformer

object HtmlToBBCodesTransformer : DocumentTransformer<StringBuilder>(
    tagsTransformers = mapOf(
        "a" to listOf(LinkWithDataAttrsTransformer, LinkTransformer),
        "h1" to listOf(HeaderTransformer),
        "h2" to listOf(HeaderTransformer),
        "h3" to listOf(HeaderTransformer),
        "h4" to listOf(HeaderTransformer),
        "h5" to listOf(HeaderTransformer),
        "h6" to listOf(HeaderTransformer),
        "b" to listOf(BoldTransformer),
        "i" to listOf(ItalicTransformer),
        "del" to listOf(StrikethroughTransformer),
        "u" to listOf(UnderlineTransformer),
        "span" to listOf(ColorTransformer, SpoilerInlineTransformer),
        "div" to listOf(SpoilerBlockTransformer),
        "img" to listOf(ImageTransform),
        "code" to listOf(CodeTransformer)
    ),
    textAppender = StringBuilder::append
)



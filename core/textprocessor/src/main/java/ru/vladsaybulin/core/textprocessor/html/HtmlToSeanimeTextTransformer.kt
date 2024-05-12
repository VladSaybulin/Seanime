package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.DocumentTransformer

object HtmlToSeanimeTextTransformer :
    DocumentTransformer<SeanimeTextBuilder>(DefaultHtmlToSeanimeTextTagTransformers)

val DefaultHtmlToSeanimeTextTagTransformers = listOf(
    ShikimoriLinkTransformer,
    ExternalLinkTransformer,
    LocalizedNameTransformer,
    BoldTextStyleTransformer,
    BoldTextStyleTransformer,
    ItalicTextStyleTransformer,
    UnderlineTextStyleTransformer,
    StrikethroughTextStyleTransformer,
    HeadlineTextStyleTransformer,
    HeaderTextStyleTransformer,
    HeaderTextStyleTransformer,
    HeaderTextStyleTransformer,
    HeaderTextStyleTransformer,
    HeaderTextStyleTransformer,
    HeaderTextStyleTransformer,
    BrTransformer,
    InlineSpoilerTransformer,
    SpoilerBlockTransformer
)

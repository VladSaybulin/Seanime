package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.textprocessor.html.AnnotatedTextBuilder
import ru.vladsaybulin.core.textprocessor.html.HtmlToAnnotatedTextTransformer
import ru.vladsaybulin.core.textprocessor.util.toHtmlDocument
import ru.vladsaybulin.database.models.common.AnnotatedTextPOJO
import ru.vladsaybulin.database.models.common.ProtoTextAnnotation

fun String.toAnnotatedTextPOJO(transformer: HtmlToAnnotatedTextTransformer): AnnotatedTextPOJO {
    val builder = AnnotatedTextBuilder()
    transformer.transform(this.toHtmlDocument(), builder)
    return builder.toAnnotatedText().let { annotatedText ->
        AnnotatedTextPOJO(
            text = annotatedText.text,
            annotations = annotatedText.annotations.map { annotation ->
                ProtoTextAnnotation(
                    start = annotation.start,
                    end = annotation.end,
                    tag = annotation.tag,
                    annotation = annotation.annotation
                )
            }
        )
    }
}
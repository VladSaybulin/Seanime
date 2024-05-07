package ru.vladsaybulin.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.model.annotatedtext.AnnotatedText

@Composable
fun AnnotatedText.toComposeAnnotatedString(): AnnotatedString {
    val colorScheme = ShikimoriTheme.colorScheme
    return remember {
        buildAnnotatedString {
            append(text)
            val annotationSequence = annotations.asSequence()
            for (textStyle in annotationSequence.filter { it.tag == "text_style" }) {
                addStyle(
                    style = getReadySpanStyle(textStyle.annotation),
                    start = textStyle.start,
                    end = textStyle.end
                )
            }

            val linkAnnotations = annotationSequence.filter { it.tag != "text_style" }

            if (!linkAnnotations.iterator().hasNext()) return@buildAnnotatedString

            val linkStyle = SpanStyle(
                color = colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )

            for (annotation in linkAnnotations) {
                addStyle(
                    style = linkStyle,
                    start = annotation.start,
                    end = annotation.end
                )

                addStringAnnotation(
                    tag = annotation.tag,
                    annotation = annotation.annotation,
                    start = annotation.start,
                    end = annotation.end
                )
            }
        }
    }
}

fun getReadySpanStyle(annotation: String): SpanStyle = when (annotation) {
    "b" -> SpanStyle(fontWeight = FontWeight.Bold)
    "i" -> SpanStyle(fontStyle = FontStyle.Italic)
    "u" -> SpanStyle(textDecoration = TextDecoration.Underline)
    "s" -> SpanStyle(textDecoration = TextDecoration.LineThrough)
    "h1" -> SpanStyle(fontSize = 2.em, fontWeight = FontWeight.Bold)
    "h2" -> SpanStyle(fontSize = 1.5.em, fontWeight = FontWeight.Bold)
    "h3" -> SpanStyle(fontSize = 1.17.em, fontWeight = FontWeight.Bold)
    "h4" -> SpanStyle(fontSize = 1.em, fontWeight = FontWeight.Bold)
    "h5" -> SpanStyle(fontSize = 0.83.em, fontWeight = FontWeight.Bold)
    "h6" -> SpanStyle(fontSize = 0.67.em, fontWeight = FontWeight.Bold)
    else -> SpanStyle()
}
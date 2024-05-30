package ru.vladsaybulin.core.ui.text

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em
import androidx.compose.ui.util.fastForEach
import ru.vladsaybulin.model.annotatedtext.SeanimeText

internal fun SeanimeText.toComposeAnnotatedString(linkColor: Color): AnnotatedString {
    return buildAnnotatedString {
        append(text)
        styles.fastForEach { addStyle(it) }
        links.fastForEach { addLinkAnnotation(it, linkColor) }
    }
}

private fun AnnotatedString.Builder.addLinkAnnotation(link: SeanimeText.Range<String>, linkColor: Color) {
    addStyle(
        style = SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline),
        start = link.start,
        end = link.end
    )
    addStringAnnotation(
        tag = link.tag,
        annotation = link.item,
        start = link.start,
        end = link.end
    )
}

private fun AnnotatedString.Builder.addStyle(styleRange: SeanimeText.Range<SeanimeText.Style>) {
    addStyle(
        style = when (val style = styleRange.item) {
            is SeanimeText.Style.ReadyStyle -> style.value.asSpanStyle()
            else -> SpanStyle()
        },
        start = styleRange.start,
        end = styleRange.end
    )
}

private fun SeanimeText.ReadyStyleValue.asSpanStyle() = when (this) {
    SeanimeText.ReadyStyleValue.H1 -> SpanStyle(fontSize = 2.em, fontWeight = FontWeight.Bold)
    SeanimeText.ReadyStyleValue.H2 -> SpanStyle(fontSize = 1.5.em, fontWeight = FontWeight.Bold)
    SeanimeText.ReadyStyleValue.H3 -> SpanStyle(fontSize = 1.17.em, fontWeight = FontWeight.Bold)
    SeanimeText.ReadyStyleValue.H4 -> SpanStyle(fontSize = 1.em, fontWeight = FontWeight.Bold)
    SeanimeText.ReadyStyleValue.H5 -> SpanStyle(fontSize = 0.83.em, fontWeight = FontWeight.Bold)
    SeanimeText.ReadyStyleValue.H6 -> SpanStyle(fontSize = 0.67.em, fontWeight = FontWeight.Bold)
    SeanimeText.ReadyStyleValue.Underline -> SpanStyle(textDecoration = TextDecoration.Underline)
    SeanimeText.ReadyStyleValue.Strikethrough -> SpanStyle(textDecoration = TextDecoration.LineThrough)
    SeanimeText.ReadyStyleValue.Bold -> SpanStyle(fontWeight = FontWeight.Bold)
    SeanimeText.ReadyStyleValue.Italic -> SpanStyle(fontStyle = FontStyle.Italic)
}
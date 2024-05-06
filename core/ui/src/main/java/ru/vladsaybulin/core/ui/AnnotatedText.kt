package ru.vladsaybulin.core.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em
import androidx.compose.ui.util.fastForEach
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.model.annotatedtext.AnnotatedText

@Composable
fun AnnotatedText(
    text: AnnotatedText,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    onClick: (tag: String, annotation: String) -> Unit,
) {
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val colorScheme = ShikimoriTheme.colorScheme
    val composeAnnotatedString = remember { text.toComposeAnnotatedString(colorScheme) }

    val pressIndicator = Modifier.pointerInput(onClick) {
        detectTapGestures { pos ->
            layoutResult.value?.let { layoutResult ->
                val offset = layoutResult.getOffsetForPosition(pos)
                composeAnnotatedString.getStringAnnotations(offset, offset)
                    .fastForEach { range -> onClick(range.tag, range.item) }
            }
        }
    }

    Text(
        text = composeAnnotatedString,
        modifier = modifier then pressIndicator,
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        inlineContent = inlineContent,
        onTextLayout = onTextLayout,
        style = style
    )
}

fun AnnotatedText.toComposeAnnotatedString(
    colorScheme: ColorScheme,
) = buildAnnotatedString {
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

@Preview
@Composable
fun AnnotatedTextPreview() {
    ShikimoriTheme {
        val annotatedText = AnnotatedText(
            text = "H1\nH2\nH3\nH4\nH5\nH6\nHeadline\nMidHeadline\nАниме Наруто, Манга Ван-Пис, ссылка на википедию",
            annotations = listOf(
                AnnotatedText.Annotation(
                    start = 0,
                    end = 3,
                    tag = "text_style",
                    annotation = "h1"
                ),
                AnnotatedText.Annotation(
                    start = 3,
                    end = 6,
                    tag = "text_style",
                    annotation = "h2"
                ),
                AnnotatedText.Annotation(
                    start = 6,
                    end = 9,
                    tag = "text_style",
                    annotation = "h3"
                ),
                AnnotatedText.Annotation(
                    start = 9,
                    end = 12,
                    tag = "text_style",
                    annotation = "h4"
                ),
                AnnotatedText.Annotation(
                    start = 12,
                    end = 15,
                    tag = "text_style",
                    annotation = "h5"
                ),
                AnnotatedText.Annotation(
                    start = 15,
                    end = 18,
                    tag = "text_style",
                    annotation = "h6"
                ),
                AnnotatedText.Annotation(
                    start = 18,
                    end = 29,
                    tag = "text_style",
                    annotation = "h5"
                ),
                AnnotatedText.Annotation(
                    start = 29,
                    end = 39,
                    tag = "text_style",
                    annotation = "h6"
                ),
                AnnotatedText.Annotation(
                    start = 45,
                    end = 51,
                    tag = "anime",
                    annotation = "20"
                ),
                AnnotatedText.Annotation(
                    start = 59,
                    end = 64,
                    tag = "manga",
                    annotation = "13"
                ),
                AnnotatedText.Annotation(
                    start = 78,
                    end = 87,
                    tag = "url",
                    annotation = "https://wikipedia.com/"
                )
            )
        )

        Surface {
            AnnotatedText(text = annotatedText, onClick = { _, _ -> })
        }
    }
}
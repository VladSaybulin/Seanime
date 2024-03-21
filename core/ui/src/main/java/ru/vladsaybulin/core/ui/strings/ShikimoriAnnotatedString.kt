package ru.vladsaybulin.core.ui.strings

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme

@Composable
@ReadOnlyComposable
fun annotatedStringBuilderContext(
    colorScheme: ColorScheme = ShikimoriTheme.colorScheme,
    defaultTextStyle: TextStyle = LocalTextStyle.current
) = AnnotatedStringBuilderContext(
    colorScheme = colorScheme,
    defaultSpanStyle = defaultTextStyle.toSpanStyle()
)

data class AnnotatedStringBuilderContext(
    val colorScheme: ColorScheme,
    val defaultSpanStyle: SpanStyle,
) {
    var currentSpanStyle = defaultSpanStyle
}

fun AnnotatedString.Builder.link(
    context: AnnotatedStringBuilderContext,
    tag: String,
    annotation: String,
    block: AnnotatedString.Builder.() -> Unit
) {
    with(context) {
        val prevSpanStyle = currentSpanStyle
        currentSpanStyle = currentSpanStyle.copy(
            color = colorScheme.primary,
            textDecoration = TextDecoration.Underline
        )

        val styleIndex = pushStyle(currentSpanStyle)
        pushStringAnnotation(tag, annotation)

        block()

        pop(styleIndex)
        currentSpanStyle = prevSpanStyle
    }
}
package ru.vladsaybulin.core.ui.entry

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
internal fun KindAndYearText(kind: String?, year: String?) {

    val textColor = LocalContentColor.current

    if (kind != null || year != null) {
        Text(
            text = buildAnnotatedString {
                kind?.also { append(kind) }

                if (year == null) return@buildAnnotatedString

                val newStyle = SpanStyle(color = textColor.copy(alpha = YearTextAlpha))
                withStyle(style = newStyle) {
                    if (kind != null) append(" \u00B7 ")
                    append(year)
                }
            }
        )
    }
}

private const val YearTextAlpha = 0.5f
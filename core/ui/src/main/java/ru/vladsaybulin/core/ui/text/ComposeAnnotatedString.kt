/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.core.ui.text

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.LineHeightStyle.Trim.Companion.LastLineBottom
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em
import androidx.compose.ui.util.fastForEach
import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue.Bold
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue.H1
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue.H2
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue.H3
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue.H4
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue.H5
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue.H6
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue.Italic
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue.Strikethrough
import ru.vladsaybulin.model.annotatedtext.SeanimeText.ReadyStyleValue.Underline

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

    when (val style = styleRange.item) {
        is SeanimeText.Style.ReadyStyle -> addReadyStyle(
            readyStyleValue = style.value,
            start = styleRange.start,
            end = styleRange.end
        )
        //TODO Other text styles
    }
}

private fun AnnotatedString.Builder.addReadyStyle(
    readyStyleValue: SeanimeText.ReadyStyleValue,
    start: Int,
    end: Int
) {
    readyStyleValue.asParagraphStyle()?.let {
        addStyle(it, start, end)
    }
    addStyle(readyStyleValue.asSpanStyle(), start, end)
}

private fun SeanimeText.ReadyStyleValue.asSpanStyle() = when (this) {
    H1 -> SpanStyle(fontSize = 2.em, fontWeight = FontWeight.Bold)
    H2 -> SpanStyle(fontSize = 1.5.em, fontWeight = FontWeight.Bold)
    H3 -> SpanStyle(fontSize = 1.17.em, fontWeight = FontWeight.Bold)
    H4 -> SpanStyle(fontSize = 1.em, fontWeight = FontWeight.Bold)
    H5 -> SpanStyle(fontSize = 0.83.em, fontWeight = FontWeight.Bold)
    H6 -> SpanStyle(fontSize = 0.67.em, fontWeight = FontWeight.Bold)
    Underline -> SpanStyle(textDecoration = TextDecoration.Underline)
    Strikethrough -> SpanStyle(textDecoration = TextDecoration.LineThrough)
    Bold -> SpanStyle(fontWeight = FontWeight.Bold)
    Italic -> SpanStyle(fontStyle = FontStyle.Italic)
}

private fun SeanimeText.ReadyStyleValue.asParagraphStyle(): ParagraphStyle? = when (this) {
    H1, H2, H3, H4, H5, H6 -> ParagraphStyle(
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Bottom,
            trim = LastLineBottom
        )
    )
    else -> null
}
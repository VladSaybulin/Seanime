package ru.vladsaybulin.model.annotatedtext

data class AnnotatedText(
    val text: String,
    val styles: List<TextRange<TextStyle>>,
    val annotations: List<TextRange<Annotation>>
) {

    data class TextRange<T: Any> (
        val start: Int,
        val end: Int,
        val item: T
    )

    sealed class TextStyle {
        data object Link : TextStyle()
        data object Bold : TextStyle()
        data object Italic : TextStyle()
        data object Underline : TextStyle()
        data object Strikethrough : TextStyle()
        data object H1 : TextStyle()
        data object H2 : TextStyle()
        data object H3 : TextStyle()
        data object H4 : TextStyle()
        data object H5 : TextStyle()
        data object H6 : TextStyle()
    }

    data class Annotation(val tag: String, val annotation: String)
}
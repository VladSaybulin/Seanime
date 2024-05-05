package ru.vladsaybulin.model.annotatedtext

data class AnnotatedText(
    val text: String,
    val annotations: List<Annotation>
) {
    data class Annotation(
        val start: Int,
        val end: Int,
        val tag: String,
        val annotation: String
    )
}
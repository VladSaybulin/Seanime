package ru.vladsaybulin.model

data class IncompleteDate(
    val day: Int?,
    val month: Int?,
    val year: Int?
)

fun IncompleteDate?.isNullOrEmpty(): Boolean {
    if (this == null) return true
    return year == null && month == null && day != null
}
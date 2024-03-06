package ru.vladsaybulin.model

enum class Rating(val serializedName: String) {
    None("")
}

fun String?.asRating() = when (this) {
    null -> Rating.None
    else -> Rating.entries.firstOrNull { it.serializedName == this } ?: Rating.None
}
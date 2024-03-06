package ru.vladsaybulin.model

enum class GenreKind(val serializedName: String) {
    Genre("genre")
}

fun String.asGenreKind() = GenreKind.entries.first { this == it.serializedName }

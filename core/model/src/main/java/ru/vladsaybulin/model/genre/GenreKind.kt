package ru.vladsaybulin.model.genre

enum class GenreKind(val serializedName: String) {
    Genre("genre"),
    Theme("theme"),
    Demographic("demographic")
}

fun String.asGenreKind() = GenreKind.entries.first { this == it.serializedName }

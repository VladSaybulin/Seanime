package ru.vladsaybulin.model.genre

enum class GenreKind(val serializedName: String) {
    Genre("genre"),
    Theme("theme"),
    Demographic("demographic"),

    //Use in search feature only
    None("")
}

fun String.asGenreKind() = GenreKind.entries.first { this == it.serializedName }

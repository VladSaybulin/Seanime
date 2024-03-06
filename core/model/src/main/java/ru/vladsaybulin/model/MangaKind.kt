package ru.vladsaybulin.model

enum class MangaKind(val serializedName: String) {
    None("")
}

fun String?.asMangaKind() = when (this) {
    null -> MangaKind.None
    else -> MangaKind.entries.firstOrNull { it.serializedName == this } ?: MangaKind.None
}

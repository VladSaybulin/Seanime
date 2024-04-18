package ru.vladsaybulin.model.common

enum class EntryType(val serializedName: String) {
    Anime("anime"),
    Manga("manga")
}

fun String.asEntryType() = EntryType.entries.first {
    it.serializedName.equals(this, ignoreCase = true)
}
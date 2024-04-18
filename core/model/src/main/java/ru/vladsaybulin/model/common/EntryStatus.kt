package ru.vladsaybulin.model.common

enum class EntryStatus(val serializedName: String) {
    Anons("anons"),
    Ongoing("ongoing"),
    Released("released"),

    //Manga only
    Paused("paused"),
    Discontinued("discontinued"),

    None("")
}

fun String?.asEntryStatus() = when (this) {
    null -> EntryStatus.None
    else -> EntryStatus.entries.firstOrNull { it.serializedName == this } ?: EntryStatus.None
}

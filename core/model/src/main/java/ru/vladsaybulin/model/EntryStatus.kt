package ru.vladsaybulin.model

enum class EntryStatus(val serializedName: String) {
    Anons("anons"),
    Ongoing("ongoing"),
    Released("released"),

    //Manga only
    Paused("paused"),
    Discontinued("discontinued"),

    None("")
}

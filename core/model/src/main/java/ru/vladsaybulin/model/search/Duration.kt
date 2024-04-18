package ru.vladsaybulin.model.search

enum class Duration(val serializedValue: String) {
    S("S"), //Less that 10 minutes
    D("D"), //Less that 30 minutes
    F("F") //More that 30 minutes
}
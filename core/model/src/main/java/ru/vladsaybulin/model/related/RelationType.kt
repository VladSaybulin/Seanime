package ru.vladsaybulin.model.related

enum class RelationType(val serializedName: String) {
    Adaptation("Adaptation"),
    AltHistory("Alternative version"),
    SideStory("Side story"),
    SpinOff("Spin-off"),
    Sequel("Sequel"),
    Prequel("Prequel"),
    Summary("Summary"),
    Character("Character"),
    Other("Other")
}

fun String?.asRelationType() = when (this) {
    null -> RelationType.Other
    else -> RelationType.entries.firstOrNull { it.serializedName == this } ?: RelationType.Other
}

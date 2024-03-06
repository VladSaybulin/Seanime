package ru.vladsaybulin.model

enum class RelationType(val serializedName: String) {
    None("")
}

fun String?.asRelationType() = when (this) {
    null -> RelationType.None
    else -> RelationType.entries.firstOrNull { it.serializedName == this } ?: RelationType.None
}

package ru.vladsaybulin.model.topic

enum class TopicEvent(val serializedValue: String) {
    Episode("episodes"),
    Unknown("")
}

fun String?.asTopicEvent() = when (this) {
    null -> TopicEvent.Unknown
    else -> TopicEvent.entries.firstOrNull { it.serializedValue == this } ?: TopicEvent.Unknown
}

package ru.vladsaybulin.model.topic

enum class TopicLinkedType(val serializedValue: String) {
    Anime("Anime"),
    Manga("Manga"),
    Ranobe("Ranobe"),
    Character("Character"),
    Person("Person"),
    Club("Club"),
    ClubPage("ClubPage"),
    Critique("Critique"),
    Review("Review"),
    Contest("Contest"),
    CosplayGallery("CosplayGallery"),
    Collection("Collection"),
    Article("Article"),
    Unknown("")
}

fun String.asTopicLinkedType() =
    TopicLinkedType.entries.first { it.serializedValue == this }
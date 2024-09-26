package ru.vladsaybulin.model.search

enum class Order (val serializedValue: String) {
    Popularity("popularity"),
    Ranked("ranked"),
    Alphabet("name"),
    Created("created_at"),
    CreatedDesc("created_at_desc"),
    Random("ranked_random"),
}
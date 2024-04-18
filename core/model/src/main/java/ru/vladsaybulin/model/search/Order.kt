package ru.vladsaybulin.model.search

enum class Order (val serializedValue: String) {
    Popularity("popularity"),
    Ranked("ranked"),
    Alphabet("alphabet"),
    Created("created"),
    CreatedDesc("created_decs"),
    Random("ranked_random"),
}
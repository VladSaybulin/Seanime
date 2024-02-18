package ru.vladsaybulin.model

enum class AnimeKind(val serializedName: String) {
    Tv("tv"),
    Movie("movie"),
    Ona("ona"),
    Ova("ova"),
    Music("music"),
    Special("special"),
    Pv("rv"),
    Cv("cv"),
    None("")
}
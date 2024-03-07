package ru.vladsaybulin.model

enum class AnimeKind(val serializedName: String) {
    Tv("tv"),
    Movie("movie"),
    Ona("ona"),
    Ova("ova"),
    Music("music"),
    Special("special"),
    TvSpecial("tv_special"),
    Pv("rv"),
    Cm("cm"),
    None("")
}

fun String?.asAnimeKind() = when(this) {
    null -> AnimeKind.None
    else -> AnimeKind.entries.firstOrNull { it.serializedName == this } ?: AnimeKind.None
}
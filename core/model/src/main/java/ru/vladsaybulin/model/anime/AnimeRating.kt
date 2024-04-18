package ru.vladsaybulin.model.anime

enum class AnimeRating(val serializedName: String) {
    G("g"),
    PG("pg"),
    PG13("pg_13"),
    R("r"),
    RPlus("r_plus"),
    RX("rx"),
    None("None")
}

fun String?.asRating() = when (this) {
    null -> AnimeRating.None
    else -> AnimeRating.entries.firstOrNull { it.serializedName == this } ?: AnimeRating.None
}
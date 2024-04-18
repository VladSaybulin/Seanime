package ru.vladsaybulin.model.search

enum class Season(val serializedValue: String) {
    Summer("summer"),
    Spring("spring"),
    Winter("winter"),
    Fall("fall")
}

sealed interface SeasonFilter {
    data class SeasonYear(
        val season: Season,
        val year: Int,
        override val id: Int? = null
    ) : SeasonFilter {
        override val serializedValue: String = "${season.serializedValue}_$year"
    }

    data class Year(
        val year: Int,
        override val id: Int? = null,
    ) : SeasonFilter {
        override val serializedValue: String = year.toString()
    }

    data class YearRange(
        val begin: Int,
        val end: Int,
        override val id: Int? = null,
    ): SeasonFilter {
        override val serializedValue: String = "${begin}_$end"
    }

    data class Decade(
        val decade: Int,
        override val id: Int? = null,
    ): SeasonFilter {
        override val serializedValue: String = "${decade}x"
    }

    val id: Int?
    val serializedValue: String
}


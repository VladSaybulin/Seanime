package ru.vladsaybulin.model.search

enum class SeasonOfYear(val serializedValue: String) {
    Winter("winter"),
    Spring("spring"),
    Summer("summer"),
    Fall("fall")
}

sealed interface TimePeriodAiring {
    data class Season(
        val seasonOfYear: SeasonOfYear,
        val year: Int,
        override val id: Int? = null
    ) : TimePeriodAiring {
        override val serializedValue: String = "${seasonOfYear.serializedValue}_$year"
    }

    data class Year(
        val year: Int,
        override val id: Int? = null,
    ) : TimePeriodAiring {
        override val serializedValue: String = year.toString()
    }

    data class YearRange(
        val begin: Int,
        val end: Int,
        override val id: Int? = null,
    ): TimePeriodAiring {
        override val serializedValue: String = "${begin}_$end"
    }

    data class Decade(
        val decade: Int,
        override val id: Int? = null,
    ): TimePeriodAiring {
        override val serializedValue: String = "${decade}x"
    }

    val id: Int?
    val serializedValue: String
}


package ru.vladsaybulin.feature.userrate

import ru.vladsaybulin.model.UserRate

sealed interface UserRateSetup {
    data class AnimeUserRate(
        override val userRate: UserRate,
        val maxEpisodes: Int,
        val released: Boolean
    ) : UserRateSetup

    val userRate: UserRate
}
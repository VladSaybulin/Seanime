package ru.vladsaybulin.feature.userrate

import androidx.lifecycle.ViewModel
import ru.vladsaybulin.model.UserRateStatus
import javax.inject.Inject

class UserRateViewModel @Inject constructor(

) : ViewModel() {
    private var setup: UserRateSetup? = null
    val requireSetup: UserRateSetup
        get() = requireNotNull(setup)

    fun setupUserRate(userRateSetup: UserRateSetup) {
        setup = userRateSetup
    }

    fun save(
        status: UserRateStatus,
        score: Int,
        episodes: Int?,
        chapters: Int?,
        volumes: Int?,
        text: String
    ) {

    }

    fun delete() {

    }
}
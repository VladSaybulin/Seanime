package ru.vladsaybulin.feature.userrate

import androidx.lifecycle.ViewModel
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.UserRateStatus
import javax.inject.Inject

class UserRateViewModel @Inject constructor(

) : ViewModel() {
    private var originalUserRate: UserRate? = null
    val requireOriginalUserRate: UserRate
        get() = requireNotNull(originalUserRate)

    fun setupUserRate(userRate: UserRate) {
        originalUserRate = userRate
    }

    fun save(
        status: UserRateStatus,
        score: Int,
        episodes: Int,
        chapters: Int,
        volumes: Int,
        text: String
    ) {

    }
}
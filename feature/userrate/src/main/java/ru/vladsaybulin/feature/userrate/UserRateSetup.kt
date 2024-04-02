package ru.vladsaybulin.feature.userrate

import ru.vladsaybulin.model.UserRate

sealed interface UserRateSetup {
    data class Success(
        val userRate: UserRate,
        val context: UserRateEditorContext,
        val enabledAutocorrect: Boolean
    ) : UserRateSetup

    data object None : UserRateSetup
}
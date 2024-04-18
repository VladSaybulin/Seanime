package ru.vladsaybulin.feature.userrate

import ru.vladsaybulin.model.userrate.UserRate

sealed interface UserRateSetup {
    data class Edit(
        val userRate: UserRate,
        val context: UserRateEditorContext,
        val enabledAutocorrect: Boolean
    ) : UserRateSetup

    data object None : UserRateSetup
}
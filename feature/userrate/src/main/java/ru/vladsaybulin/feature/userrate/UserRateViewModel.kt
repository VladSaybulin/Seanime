package ru.vladsaybulin.feature.userrate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.core.domain.GetEnableAutocorrectUserRateUseCase
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.UserRateValues
import javax.inject.Inject

class UserRateViewModel @Inject constructor(
    getEnabledAutocorrectUseCase: GetEnableAutocorrectUserRateUseCase
) : ViewModel() {

    private val userRateWithContext =
        MutableSharedFlow<Pair<UserRate, UserRateEditorContext>>()

    val setup = combine(
        getEnabledAutocorrectUseCase(),
        userRateWithContext
    ) { enabledAutocorrect, (userRate, context) ->
        UserRateSetup.Success(
            userRate = userRate,
            context = context,
            enabledAutocorrect = enabledAutocorrect
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UserRateSetup.None
        )

    fun setupUserRate(userRate: UserRate, context: UserRateEditorContext) {
        userRateWithContext.tryEmit(userRate to context)
    }

    fun save(userRateValues: UserRateValues) {

    }

    fun delete() {

    }
}

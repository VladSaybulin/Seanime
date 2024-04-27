package ru.vladsaybulin.feature.userrate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.GetEnableAutocorrectUserRateUseCase
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.model.userrate.UserRateValues
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import javax.inject.Inject

@HiltViewModel
class UserRateViewModel @Inject constructor(
    getEnabledAutocorrectUseCase: GetEnableAutocorrectUserRateUseCase,
    private val userRateRepository: UserRateRepository
) : ViewModel() {

    private val _userRateWithEntry = MutableStateFlow<UserRateWithEntry?>(null)
    val userRateWithEntry= _userRateWithEntry.asStateFlow()

    val autocorrectUserRate = getEnabledAutocorrectUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    fun setUserRate(newUserRateWithEntry: UserRateWithEntry) {
        _userRateWithEntry.tryEmit(newUserRateWithEntry)
    }

    fun save(userRateValues: UserRateValues) {
        val userRateId = getUserRateId() ?: return
        viewModelScope.launch {
            userRateRepository.updateUserRate(userRateId, userRateValues)
        }
    }

    fun delete() {
        val userRateId = getUserRateId() ?: return
        viewModelScope.launch {
            userRateRepository.deleteUserRate(userRateId)
        }
    }

    private fun getUserRateId(): Long? = userRateWithEntry.value?.userRate?.id
}
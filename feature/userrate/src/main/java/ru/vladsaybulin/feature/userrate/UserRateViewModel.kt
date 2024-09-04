package ru.vladsaybulin.feature.userrate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.model.userrate.UserRateValues
import javax.inject.Inject

@HiltViewModel
class UserRateViewModel @Inject constructor(
    private val userRateRepository: UserRateRepository
) : ViewModel() {

    fun save(
        userRateId: Long,
        userRateValues: UserRateValues
    ) {
        viewModelScope.launch {
            userRateRepository.updateUserRate(userRateId, userRateValues)
        }
    }

    fun delete(userRateId: Long) {
        viewModelScope.launch {
            userRateRepository.deleteUserRate(userRateId)
        }
    }
}
package ru.vladsaybulin.feature.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import ru.vladsaybulin.core.domain.GetPagedUserRatesUseCase
import ru.vladsaybulin.data.repository.AuthRepository
import ru.vladsaybulin.feature.list.navigation.ListArgs
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus
import javax.inject.Inject

@HiltViewModel
class MyListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getPagedUserRatesUseCase: GetPagedUserRatesUseCase,
    authRepository: AuthRepository,
) : ViewModel() {

    private val args =  ListArgs(savedStateHandle)

    val authState = authRepository.authState

    private val _uiState = MutableStateFlow(
        ListUiState(
            entryType = args.entryType ?: EntryType.Anime,
            userRateStatus = args.userRateStatus ?: UserRateStatus.Watching
        )
    )
    val uiState = _uiState.asStateFlow()

    val userRatesPagingData = _uiState.flatMapLatest { (type, status) ->
        getPagedUserRatesUseCase(type, status)
    }.cachedIn(viewModelScope)

    fun onEntryTypeChanged(entryType: EntryType) {
        _uiState.update { it.copy(entryType = entryType) }
    }

    fun onUserRateStatusChanged(userRateStatus: UserRateStatus) {
        _uiState.update { it.copy(userRateStatus = userRateStatus) }
    }
}
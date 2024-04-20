package ru.vladsaybulin.feature.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.core.domain.GetPagedUserRatesUseCase
import ru.vladsaybulin.feature.list.navigation.ListArgs
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus
import javax.inject.Inject

@HiltViewModel
class MyListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPagedUserRatesUseCase: GetPagedUserRatesUseCase
) : ViewModel() {

    private val args =  ListArgs(savedStateHandle)

    private val _entryType = MutableStateFlow(args.entryType ?: EntryType.Anime)
    val entryType = _entryType.asStateFlow()

    private val _userRateStatus = MutableStateFlow(args.userRateStatus ?: UserRateStatus.Watching)
    val userRateStatus = _userRateStatus.asStateFlow()

    val userRatesPagingData = combine(
        _entryType,
        _userRateStatus
    ) { type, status ->
        getPagedUserRatesUseCase(type, status)
    }
        .flattenMerge()
        .cachedIn(viewModelScope)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            PagingData.empty()
        )

    fun onEntryTypeChanged(newEntryType: EntryType) {
        _entryType.value = newEntryType
    }

    fun onUserRateStatusChanged(newUserRateStatus: UserRateStatus) {
        _userRateStatus.value = newUserRateStatus
    }
}
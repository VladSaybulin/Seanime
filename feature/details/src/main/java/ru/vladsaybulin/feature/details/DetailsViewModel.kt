package ru.vladsaybulin.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.GetEntryDetailsUseCase
import ru.vladsaybulin.feature.details.navigation.DetailsArgs
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getEntryDetailsUseCase: GetEntryDetailsUseCase
) : ViewModel() {

    private val args = DetailsArgs(savedStateHandle)

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        refresh()
    }

    suspend fun onRefresh() {
        refresh().join()
    }

    fun onRetry() {
        refresh()
    }

    private fun refresh() = viewModelScope.launch {
        _uiState.value = getEntryDetailsUseCase(args.entryType, args.entryId)
            .map { details ->
                DetailsUiState(
                    details.anime!!,
                    details.userRate,
                    details.similarEntries
                ) as DetailsUiState
            }
            .catch { emit(DetailsUiState.Error(it)) }
            .first()
    }
}

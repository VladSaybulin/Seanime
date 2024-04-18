package ru.vladsaybulin.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.CreateUserRateUseCase
import ru.vladsaybulin.core.domain.GetEnableAutocorrectUserRateUseCase
import ru.vladsaybulin.core.domain.GetEntryDetailsUseCase
import ru.vladsaybulin.data.repository.AuthRepository
import ru.vladsaybulin.feature.details.navigation.DetailsArgs
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.model.UserRateStatus
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getEntryDetailsUseCase: GetEntryDetailsUseCase,
    private val createUserRateUseCaseProvider: Provider<CreateUserRateUseCase>,
    getEnableAutocorrectUserRateUseCase: GetEnableAutocorrectUserRateUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val args = DetailsArgs(savedStateHandle)

    private val entryDetails = MutableSharedFlow<EntryDetails>(replay = 1)

    val enabledAutocorrectStatus = getEnableAutocorrectUserRateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    val uiState = entryDetails
        .map { details -> details.toUiState() }
        .catch { emit(DetailsUiState.Error(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DetailsUiState.Loading
        )

    suspend fun refresh() {
        getEntryDetailsUseCase(args.entryType, args.entryId).collect{
            entryDetails.emit(it)
        }
    }

    fun onRetry() {
        viewModelScope.launch {
            refresh()
        }
    }

    fun createUserRate(status: UserRateStatus) {
        viewModelScope.launch {
            createUserRateUseCaseProvider.get().invoke(
                userRateStatus = status,
                entryDetails = entryDetails.first()
            )
        }
    }

    fun isAuthorized() = authRepository.isAuthorized()

}
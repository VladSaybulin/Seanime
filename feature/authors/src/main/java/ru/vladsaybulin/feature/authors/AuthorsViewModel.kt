package ru.vladsaybulin.feature.authors

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.core.domain.GetAuthorsUseCase
import ru.vladsaybulin.feature.authors.navigation.toTitleAuthorsArgs
import ru.vladsaybulin.model.person.PersonWithRoles
import javax.inject.Inject

@HiltViewModel
class AuthorsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAuthorsUseCase: GetAuthorsUseCase
) : ViewModel() {

    private val args = savedStateHandle.toTitleAuthorsArgs()

    val uiState = getAuthorsUseCase(args.entryType, args.entryId)
        .map { AuthorsUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AuthorsUiState.Loading
        )
}

sealed class AuthorsUiState {
    data object Loading : AuthorsUiState()

    data class Success(val authors: List<PersonWithRoles>) : AuthorsUiState()
}
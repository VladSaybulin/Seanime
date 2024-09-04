package ru.vladsaybulin.feature.character

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.data.repository.CharacterRepository
import ru.vladsaybulin.feature.character.navigation.CharacterDetailsScreenRoute
import ru.vladsaybulin.model.character.CharacterDetails
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    characterRepository: CharacterRepository,
): ViewModel() {

    private val route = savedStateHandle.toRoute<CharacterDetailsScreenRoute>()

    val uiState = characterRepository.getCharacterDetails(route.characterId)
        .map<CharacterDetails, CharacterDetailsUiState> { CharacterDetailsUiState.Success(it) }
        .catch {
            emit(CharacterDetailsUiState.Error(it))
            it.printStackTrace()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CharacterDetailsUiState.Loading
        )
}
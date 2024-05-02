package ru.vladsaybulin.feature.character

import ru.vladsaybulin.model.character.CharacterDetails

sealed class CharacterDetailsUiState {

    data object Loading : CharacterDetailsUiState()

    data class Success(val characterDetails: CharacterDetails) : CharacterDetailsUiState()

    data class Error(val throwable: Throwable) : CharacterDetailsUiState()

}
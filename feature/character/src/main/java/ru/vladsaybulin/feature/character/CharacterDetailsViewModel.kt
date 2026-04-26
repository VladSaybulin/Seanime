/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
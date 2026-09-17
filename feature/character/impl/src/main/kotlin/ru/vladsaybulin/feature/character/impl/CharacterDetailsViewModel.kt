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

package ru.vladsaybulin.feature.character.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.common.ui.tryRefresh
import ru.vladsaybulin.core.domain.character.GetCharacterDetailsStreamUseCase
import ru.vladsaybulin.core.domain.character.RefreshCharacterDetailsUseCase
import ru.vladsaybulin.feature.character.api.navigation.CharacterNavKey
import ru.vladsaybulin.model.character.CharacterDetails

@HiltViewModel(assistedFactory = CharacterDetailsViewModel.Factory::class)
class CharacterDetailsViewModel @AssistedInject constructor(
    characterDetailsStream: GetCharacterDetailsStreamUseCase,
    private val refreshCharacterDetails: RefreshCharacterDetailsUseCase,
    @Assisted private val key: CharacterNavKey
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(key: CharacterNavKey): CharacterDetailsViewModel
    }

    val uiState = characterDetailsStream(key.characterId)
        .onStart { internalRefresh(false) }
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

    private suspend fun internalRefresh(forceRefresh: Boolean) {
        tryRefresh(
            catch = {  }
        ) {
            refreshCharacterDetails(key.characterId, forceRefresh)
        }
    }
}
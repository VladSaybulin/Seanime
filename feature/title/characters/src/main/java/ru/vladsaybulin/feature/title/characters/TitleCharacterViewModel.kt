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

package ru.vladsaybulin.feature.title.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.feature.title.characters.navigation.TitleCharactersScreenRoute
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.EntryType
import javax.inject.Inject

@HiltViewModel
class TitleCharacterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    animeRepository: AnimeRepository,
    mangaRepository: MangaRepository
) : ViewModel() {

    private val route: TitleCharactersScreenRoute = savedStateHandle.toRoute()

    internal val uiState = when (route.titleType) {
        EntryType.Anime -> animeRepository.getAllAnimeCharacters(route.titleId)
        EntryType.Manga -> mangaRepository.getAllMangaCharacters(route.titleId)
    }
        .map<List<CharacterWithRole>, TitleCharactersUiState> { TitleCharactersUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TitleCharactersUiState.Loading
        )

}

internal sealed class TitleCharactersUiState {
    data object Loading : TitleCharactersUiState()

    class Success(val characters: List<CharacterWithRole>) : TitleCharactersUiState()
}
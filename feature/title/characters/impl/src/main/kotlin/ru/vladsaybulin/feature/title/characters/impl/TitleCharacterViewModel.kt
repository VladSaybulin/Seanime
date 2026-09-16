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

package ru.vladsaybulin.feature.title.characters.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.feature.title.characters.api.navigation.TitleCharactersNavKey
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.EntryType
import javax.inject.Inject

@HiltViewModel
class TitleCharacterViewModel @Inject constructor(
    animeRepository: AnimeRepository,
    mangaRepository: MangaRepository,
    @Assisted key: TitleCharactersNavKey
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(key: TitleCharactersNavKey): TitleCharacterViewModel
    }

    internal val uiState = when (key.titleType) {
        EntryType.Anime -> animeRepository.getAllAnimeCharacters(key.titleId)
        EntryType.Manga -> mangaRepository.getAllMangaCharacters(key.titleId)
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
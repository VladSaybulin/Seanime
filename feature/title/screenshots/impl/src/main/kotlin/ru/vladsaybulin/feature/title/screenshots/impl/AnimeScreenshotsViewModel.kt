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

package ru.vladsaybulin.feature.title.screenshots.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.core.domain.repository.AnimeRepository
import ru.vladsaybulin.feature.title.screenshots.api.navigation.AnimeScreenshotsNavKey
import ru.vladsaybulin.model.common.Image

@HiltViewModel(assistedFactory = AnimeScreenshotsViewModel.Factory::class)
class AnimeScreenshotsViewModel @AssistedInject constructor(
    animeRepository: AnimeRepository,
    @Assisted key: AnimeScreenshotsNavKey
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(key: AnimeScreenshotsNavKey): AnimeScreenshotsViewModel
    }

    internal val uiState = animeRepository.getAnimeScreenshots(key.animeId)
        .map<List<Image>, AnimeScreenshotsUiState> { AnimeScreenshotsUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AnimeScreenshotsUiState.Loading
        )
}

internal sealed class AnimeScreenshotsUiState {
    data object Loading : AnimeScreenshotsUiState()

    class Success(val screenshots: List<Image>) : AnimeScreenshotsUiState()
}
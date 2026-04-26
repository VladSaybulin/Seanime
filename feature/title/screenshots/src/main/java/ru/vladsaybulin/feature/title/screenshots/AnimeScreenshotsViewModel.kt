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

package ru.vladsaybulin.feature.title.screenshots

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.feature.title.screenshots.navigation.AnimeScreenshotsScreenRoute
import ru.vladsaybulin.model.common.Image
import javax.inject.Inject

@HiltViewModel
class AnimeScreenshotsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    animeRepository: AnimeRepository
) : ViewModel() {
    private val route = savedStateHandle.toRoute<AnimeScreenshotsScreenRoute>()

    internal val uiState = animeRepository.getAnimeScreenshots(route.animeId)
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
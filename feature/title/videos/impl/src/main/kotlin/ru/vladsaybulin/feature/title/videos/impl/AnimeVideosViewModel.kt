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

package ru.vladsaybulin.feature.title.videos.impl

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.feature.title.videos.api.navigation.AnimeVideosNavKey
import ru.vladsaybulin.model.anime.Video
import javax.inject.Inject

@HiltViewModel
class AnimeVideosViewModel @Inject constructor(
    animeRepository: AnimeRepository,
    @Assisted key: AnimeVideosNavKey
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(key: AnimeVideosNavKey): AnimeVideosViewModel
    }

    internal val uiState = animeRepository.getAllAnimeVideos(key.animeId)
        .map { AnimeVideosUIState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AnimeVideosUIState.Loading
        )
}

@Stable
internal sealed class AnimeVideosUIState {

    @Immutable
    data object Loading : AnimeVideosUIState()

    @Immutable
    class Success(val videos: List<Video>) : AnimeVideosUIState()
}
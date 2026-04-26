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

package ru.vladsaybulin.feature.title.related

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
import ru.vladsaybulin.feature.title.related.navigation.TitleRelatedScreenRoute
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.related.RelatedTitle
import javax.inject.Inject

@HiltViewModel
class TitleRelatedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    animeRepository: AnimeRepository,
    mangaRepository: MangaRepository
) : ViewModel() {

    private val route: TitleRelatedScreenRoute = savedStateHandle.toRoute()

    val uiState = when (route.titleType) {
        EntryType.Anime -> animeRepository.getAllAnimeRelatedTitles(route.titleId)
        EntryType.Manga -> mangaRepository.getAllMangaRelatedTitles(route.titleId)
    }
        .map<List<RelatedTitle>, TitleRelatedUiState> { TitleRelatedUiState.Success(it) }
        .stateIn(
            initialValue = TitleRelatedUiState.Loading,
            started = SharingStarted.WhileSubscribed(5000),
            scope = viewModelScope,
        )

}

sealed class TitleRelatedUiState {
    data object Loading : TitleRelatedUiState()

    class Success(val relatedTitles: List<RelatedTitle>) : TitleRelatedUiState()
}
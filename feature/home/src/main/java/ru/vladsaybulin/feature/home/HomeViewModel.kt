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

package ru.vladsaybulin.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.home.GetInProgressUserRatesUseCase
import ru.vladsaybulin.core.domain.home.GetNewsTopicsStreamUseCase
import ru.vladsaybulin.core.domain.home.GetOngoingAnimesStreamUseCase
import ru.vladsaybulin.core.domain.home.UpdateInProgressUserRatesUseCase
import ru.vladsaybulin.core.domain.home.UpdateNewsTopicsUseCase
import ru.vladsaybulin.core.domain.home.UpdateOngoingAnimesUseCase
import ru.vladsaybulin.core.domain.shared.GetAuthStateStreamUseCase
import ru.vladsaybulin.data.repository.UserRepository
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.topic.Topic
import ru.vladsaybulin.model.user.BriefUser
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getOngoingAnimesStreamUseCase: GetOngoingAnimesStreamUseCase,
    getInProgressUserRatesUseCase: GetInProgressUserRatesUseCase,
    getNewsTopicsStreamUseCase: GetNewsTopicsStreamUseCase,
    userRepository: UserRepository,
    getAuthStateStreamUseCase: GetAuthStateStreamUseCase,
    private val updateOngoingAnimesUseCase: UpdateOngoingAnimesUseCase,
    private val updateNewsTopicsUseCase: UpdateNewsTopicsUseCase,
    private val updateInProgressUserRatesUseCase: UpdateInProgressUserRatesUseCase
) : ViewModel() {

    private val refreshingUserRate = getAuthStateStreamUseCase()
        .drop(1)
        .filter { it == ShikimoriAuthState.LOGGED_IN }
        .onEach { updateInProgressUserRatesUseCase() }

    private var refreshingUserRatesJob: Job? = null

    val uiState = combine<List<UserRateWithEntry>, List<Anime>, List<Topic>, BriefUser?, HomeUiState>(
        getInProgressUserRatesUseCase(),
        getOngoingAnimesStreamUseCase(),
        getNewsTopicsStreamUseCase(),
        userRepository.getMeStream()
    ) { userRates, ongoingAnime, newsTopics, me ->
        HomeUiState.Success(
            inProgressUserRates = userRates.toImmutableList(),
            ongoings = ongoingAnime.toImmutableList(),
            newsTopics = newsTopics.toImmutableList(),
            me = me
        )
    }
        .onStart { internalRefresh() }
        .onStart { refreshingUserRatesJob = refreshingUserRate.launchIn(viewModelScope) }
        .onCompletion { refreshingUserRatesJob?.cancel() }
        .catch { emit(HomeUiState.Error(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )

    private suspend fun internalRefresh() {
        coroutineScope {
            launch { updateOngoingAnimesUseCase(false) }
            launch { updateNewsTopicsUseCase(false) }
            launch { updateInProgressUserRatesUseCase() }
        }
    }
}
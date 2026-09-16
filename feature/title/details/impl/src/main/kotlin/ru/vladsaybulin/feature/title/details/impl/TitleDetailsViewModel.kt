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

package ru.vladsaybulin.feature.title.details.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.GetEnableAutocorrectUserRateUseCase
import ru.vladsaybulin.core.domain.titledetails.GetFirstAnimeVideosStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.GetFirstTitleRelatedStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.GetUserRateStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.RefreshTitleDetailsUseCase
import ru.vladsaybulin.core.domain.titledetails.RefreshTitleDetailsUseCase.RefreshCompleted
import ru.vladsaybulin.core.domain.titledetails.RefreshTitleDetailsUseCase.RefreshCompleted.Details
import ru.vladsaybulin.core.domain.titledetails.RefreshTitleDetailsUseCase.RefreshCompleted.Roles
import ru.vladsaybulin.core.domain.titledetails.RefreshTitleDetailsUseCase.RefreshCompleted.Similar
import ru.vladsaybulin.core.domain.titledetails.RefreshTitleDetailsUseCase.RefreshCompleted.SkipRefresh
import ru.vladsaybulin.core.domain.titledetails.UserRateResult
import ru.vladsaybulin.core.domain.repository.AnimeRepository
import ru.vladsaybulin.core.domain.repository.MangaRepository
import ru.vladsaybulin.core.domain.repository.UserRateRepository
import ru.vladsaybulin.feature.list.title.details.navigation.TitleDetailsNavKey
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateValues

@HiltViewModel(assistedFactory = TitleDetailsViewModel.Factory::class)
class TitleDetailsViewModel @AssistedInject constructor(
    animeRepository: Lazy<AnimeRepository>,
    mangaRepository: Lazy<MangaRepository>,
    private val userRateRepository: UserRateRepository,
    private val refreshTitleDetailsUseCase: RefreshTitleDetailsUseCase,
    getFirstTitleRelatedStreamUseCase: GetFirstTitleRelatedStreamUseCase,
    getFirstAnimeVideosStreamUseCase: Lazy<GetFirstAnimeVideosStreamUseCase>,
    getEnableAutocorrectUserRateUseCase: GetEnableAutocorrectUserRateUseCase,
    getUserRateStreamUseCase: GetUserRateStreamUseCase,
    @Assisted private val key: TitleDetailsNavKey
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(key: TitleDetailsNavKey): TitleDetailsViewModel
    }

    val enabledAutocorrectStatus = getEnableAutocorrectUserRateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    private val initialRefreshing = refreshTitleDetailsUseCase(key.titleType, key.titleId, false)
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily
        )

    val detailsState: StateFlow<TitleDetailsState> = when (key.titleType) {
        EntryType.Anime -> combine(
            animeRepository.get().getAnimeDetailsStream(key.titleId),
            getFirstTitleRelatedStreamUseCase(key.titleType, key.titleId),
            animeRepository.get().getAnimeScreenshots(key.titleId),
            getFirstAnimeVideosStreamUseCase.get().invoke(key.titleId)
        ) { details, relatedSlice, screenshots, videosSlice ->
            successTitleDetails(
                animeDetails = details,
                relatedSlice = relatedSlice,
                screenshots = screenshots,
                videosSlice = videosSlice
            )
        }

        EntryType.Manga -> combine(
            mangaRepository.get().getMangaDetailsStream(key.titleId),
            getFirstTitleRelatedStreamUseCase(key.titleType, key.titleId)
        ) { details, relatedSlice ->
            successTitleDetails(
                mangaDetails = details,
                relatedSlice = relatedSlice
            )
        }
    }
        //Await complete Details refreshing
        .onStart { initialRefreshing.first { it.equalsOrSkipped(Details) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TitleDetailsState.Loading
        )

    val rolesState: StateFlow<RolesState> = when (key.titleType) {
        EntryType.Anime -> combine(
            animeRepository.get().getAnimeMainCharactersStream(key.titleId),
            animeRepository.get().getAnimeMainAuthorsStream(key.titleId),
            RolesState::Success
        )

        EntryType.Manga -> combine(
            mangaRepository.get().getMangaMainCharactersStream(key.titleId),
            mangaRepository.get().getMangaMainAuthorsStream(key.titleId),
            RolesState::Success
        )
    }
        //Await complete Roles refreshing
        .onStart { initialRefreshing.first { it.equalsOrSkipped(Roles) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RolesState.Loading
        )

    val similarState: StateFlow<SimilarState> = when (key.titleType) {
        EntryType.Anime -> animeRepository.get().getSimilarAnimes(key.titleId)
            .map { if (it.isEmpty()) SimilarState.Empty else SimilarState.Animes(it) }

        EntryType.Manga -> mangaRepository.get().getSimilarMangasStream(key.titleId)
            .map { if (it.isEmpty()) SimilarState.Empty else SimilarState.Mangas(it) }
    }
        //Await complete Similar refreshing
        .onStart { initialRefreshing.first { it.equalsOrSkipped(Similar) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SimilarState.Loading
        )

    val userRateState = getUserRateStreamUseCase(key.titleType, key.titleId).map {
        when (it) {
            UserRateResult.NotAuthorized -> UserRateState.NotAuthorized
            is UserRateResult.Success -> it.userRate?.let(UserRateState::Success) ?: UserRateState.NoUserRate
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserRateState.Loading
    )

    suspend fun refresh() {
        refreshJob().join()
    }

    fun onRetry() {
        refreshJob()
    }

    fun createUserRate(status: UserRateStatus) {
        viewModelScope.launch {
            userRateRepository.createUserRate(
                entryType = key.titleType,
                entryId = key.titleId,
                userRateValues = UserRateValues(status = status)
            )
        }
    }

    private fun refreshJob(): Job =
        refreshTitleDetailsUseCase(key.titleType, key.titleId, true).launchIn(viewModelScope)

}

private fun RefreshCompleted.equalsOrSkipped(refreshCompleted: RefreshCompleted) =
    this == SkipRefresh || this == refreshCompleted
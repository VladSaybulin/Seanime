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

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.app.GetSessionStateStreamUseCase
import ru.vladsaybulin.core.domain.common.BuildUserRateContextUseCase
import ru.vladsaybulin.core.domain.shared.LoginViaShikimoriUseCase
import ru.vladsaybulin.core.domain.titledetails.GetRelatedTitlesStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.GetSimilarTitlesStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.GetTitleAuthorsStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.GetTitleBriefStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.GetTitleCharactersStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.GetTitleDetailsStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.GetUserRateByTitleStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.RefreshTitleDetailsUseCase
import ru.vladsaybulin.feature.list.title.details.navigation.TitleDetailsNavKey
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.auth.SessionState
import ru.vladsaybulin.model.userrate.UserRateContext

@HiltViewModel(assistedFactory = TitleDetailsViewModel.Factory::class)
class TitleDetailsViewModel @AssistedInject constructor(
    getTitleBrief: GetTitleBriefStreamUseCase,
    getTitleDetails: GetTitleDetailsStreamUseCase,
    getRelatedTitles: GetRelatedTitlesStreamUseCase,
    getCharacters: GetTitleCharactersStreamUseCase,
    getAuthors: GetTitleAuthorsStreamUseCase,
    getSimilarTitles: GetSimilarTitlesStreamUseCase,
    getUserRateByTitle: GetUserRateByTitleStreamUseCase,
    getSessionState: GetSessionStateStreamUseCase,
    private val buildUserRateContext: Lazy<BuildUserRateContextUseCase>,
    private val login: Lazy<LoginViaShikimoriUseCase>,
    private val refreshTitleDetails: RefreshTitleDetailsUseCase,
    @Assisted private val key: TitleDetailsNavKey
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(key: TitleDetailsNavKey): TitleDetailsViewModel
    }

    val titleType = key.titleType
    val titleId = key.titleId

    private val brief = getTitleBrief(titleType, titleId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = key.title.data
    )

    private val details = getTitleDetails(titleType, titleId)
        .shareIn(viewModelScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)

    val headerState: StateFlow<HeaderState> = brief
        .filterNotNull()
        .map {
            HeaderData(
                poster = it.poster,
                name = it.name,
                nameRu = it.nameRu
            )
        }.asLoadState()

    val infoState: StateFlow<InfoState> = combine(
        brief.filterNotNull(),
        details
    ) { brief, details ->
        InfoData(
            kind = brief.kind,
            titleType = brief.type,
            status = brief.status,
            score = brief.score,
            episodes = brief.episodes,
            episodesAired = brief.episodesAired,
            episodeDuration = details.episodeDuration ?: 0,
            chapters = brief.chapters,
            volumes = brief.volumes,
            airedOn = brief.airedOn,
            releasedOn = brief.releasedOn,
            season = details.season,
            rating = details.rating ?: AnimeRating.None,
            nextEpisodeAt = details.nextEpisodeAt,
            studios = details.studios ?: emptyList(),
            publishers = details.publishers ?: emptyList(),
            genres = details.genres,
            description = details.description,
            descriptionSource = details.descriptionSource,
            scoreStats = details.scoreStats ?: emptyList(),
            statusStats = details.userRateStatusStats ?: emptyList()
        )
    }.asLoadState()

    val authorsState: StateFlow<AuthorsState> = getAuthors(titleType, titleId)
        .map { ExpandableSectionState(it) }
        .asLoadState()

    val charactersState = getCharacters(titleType, titleId)
        .map { ExpandableSectionState(it) }
        .asLoadState()

    val relatedTitlesState: StateFlow<RelatedTitlesState> = getRelatedTitles(titleType, titleId)
        .map { ExpandableSectionState(it) }
        .asLoadState()

    val similarTitlesState: StateFlow<SimilarTitlesState> = getSimilarTitles(titleType, titleId)
        .asLoadState()

    val animeMediaState: StateFlow<AnimeMediaState> = details
        .map { details ->
            AnimeMediaData(
                screenshots = details.screenshots ?: emptyList(),
                videos = details.videos ?: emptyList()
            )
        }.asLoadState()

    val userRateState: StateFlow<UserRateState> = getUserRateByTitle(titleType, titleId)
        .asLoadState()

    val sessionState: StateFlow<SessionState> = getSessionState()

    init {
        viewModelScope.launch {
            onRefresh(false)
        }
    }

    fun onRefresh(forceRefresh: Boolean = true) {
        viewModelScope.launch {
            refreshTitleDetails(titleType, titleId, forceRefresh)
                .collect {
                    Log.e("TitleDetailsViewModel", "Refresh error", it)
                }
        }
    }

    fun onLoginClick() {
        login.get().invoke()
    }

    fun buildUserRateContext(): UserRateContext? {
        val brief = brief.replayCache.firstOrNull() ?: return null
        return buildUserRateContext.get().invoke(brief)
    }

    fun isAuthenticated(): Boolean {
        return sessionState.value == SessionState.Authenticated
    }

    private inline fun <reified T> Flow<T>.asLoadState(): StateFlow<TitleDetailsLoadState<T>> = this
        .map { TitleDetailsLoadState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TitleDetailsLoadState.Loading()
        )
}
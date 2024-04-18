package ru.vladsaybulin.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.TopicsRepository
import ru.vladsaybulin.data.repository.UserRateRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    animeRepository: AnimeRepository,
    topicsRepository: TopicsRepository,
    userRateRepository: UserRateRepository
) : ViewModel() {

    val uiState = combine(
        userRateRepository.getLastInProgressUserRates(),
        animeRepository.getOngoingAnime(10),
        topicsRepository.getNewsTopics()
    ) { userRates, ongoingAnime, newsTopics ->
        HomeUiState.Success(
            inProgressUserRates = userRates.toImmutableList(),
            ongoings = ongoingAnime.toImmutableList(),
            newsTopics = newsTopics.toImmutableList()
        ) as HomeUiState
    }
        .catch { emit(HomeUiState.Error(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = HomeUiState.Loading
        )
}
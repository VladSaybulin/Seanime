package ru.vladsaybulin.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.GetEntryDetailsUseCase
import ru.vladsaybulin.feature.details.model.DetailsInfo
import ru.vladsaybulin.feature.details.navigation.DetailsArgs
import ru.vladsaybulin.feature.userrate.UserRateSetup
import ru.vladsaybulin.model.EntryStatus
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getEntryDetailsUseCase: GetEntryDetailsUseCase
) : ViewModel() {

    private val args = DetailsArgs(savedStateHandle)

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun getUserRateSetup(): UserRateSetup? {
        val currentState = _uiState.value
        if (currentState !is DetailsUiState.Success) return null
        if (currentState.userRate == null) return null
        val episodesInfo = currentState.info
            .firstOrNull { it is DetailsInfo.AnimeKindEpisodes } as DetailsInfo.AnimeKindEpisodes?
        val statusInfo = currentState.info
            .firstOrNull { it is DetailsInfo.StatusDates } as DetailsInfo.StatusDates
        val maxEpisodes = when {
            episodesInfo == null -> Int.MAX_VALUE
            episodesInfo.episodesAired > 0 -> episodesInfo.episodesAired
            episodesInfo.episodes > 0 -> episodesInfo.episodes
            else -> Int.MAX_VALUE
        }
        return UserRateSetup.AnimeUserRate(
            userRate = currentState.userRate,
            maxEpisodes = maxEpisodes,
            released = statusInfo.status == EntryStatus.Released
        )
    }


    suspend fun onRefresh() {
        refresh().join()
    }

    fun onRetry() {
        refresh()
    }

    private fun refresh() = viewModelScope.launch {
        _uiState.value = getEntryDetailsUseCase(args.entryType, args.entryId)
            .map { details ->
                DetailsUiState(
                    details.anime!!,
                    details.userRate,
                    details.similarEntries
                ) as DetailsUiState
            }
            .catch { emit(DetailsUiState.Error(it)) }
            .first()
    }
}

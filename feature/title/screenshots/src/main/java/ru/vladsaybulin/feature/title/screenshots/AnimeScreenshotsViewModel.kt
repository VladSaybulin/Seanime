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
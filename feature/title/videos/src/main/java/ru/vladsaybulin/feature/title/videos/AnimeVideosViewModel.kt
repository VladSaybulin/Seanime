package ru.vladsaybulin.feature.title.videos

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.feature.title.videos.navigation.AnimeVideosScreenRoute
import ru.vladsaybulin.model.anime.Video
import javax.inject.Inject

@HiltViewModel
class AnimeVideosViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    animeRepository: AnimeRepository
) : ViewModel() {
    private val route: AnimeVideosScreenRoute = savedStateHandle.toRoute()

    internal val uiState = animeRepository.getAllAnimeVideos(route.animeId)
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
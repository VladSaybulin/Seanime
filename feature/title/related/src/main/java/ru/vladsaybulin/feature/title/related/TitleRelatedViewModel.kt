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
import ru.vladsaybulin.model.related.RelatedEntry
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
        .map<List<RelatedEntry>, TitleRelatedUiState> { TitleRelatedUiState.Success(it) }
        .stateIn(
            initialValue = TitleRelatedUiState.Loading,
            started = SharingStarted.WhileSubscribed(5000),
            scope = viewModelScope,
        )

}

sealed class TitleRelatedUiState {
    data object Loading : TitleRelatedUiState()

    class Success(val relatedTitles: List<RelatedEntry>) : TitleRelatedUiState()
}
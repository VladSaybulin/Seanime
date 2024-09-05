package ru.vladsaybulin.feature.title.characters

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
import ru.vladsaybulin.feature.title.characters.navigation.TitleCharactersScreenRoute
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.EntryType
import javax.inject.Inject

@HiltViewModel
class TitleCharacterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    animeRepository: AnimeRepository,
    mangaRepository: MangaRepository
) : ViewModel() {

    private val route: TitleCharactersScreenRoute = savedStateHandle.toRoute()

    internal val uiState = when (route.titleType) {
        EntryType.Anime -> animeRepository.getAllAnimeCharacters(route.titleId)
        EntryType.Manga -> mangaRepository.getAllMangaCharacters(route.titleId)
    }
        .map<List<CharacterWithRole>, TitleCharactersUiState> { TitleCharactersUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TitleCharactersUiState.Loading
        )

}

internal sealed class TitleCharactersUiState {
    data object Loading : TitleCharactersUiState()

    class Success(val characters: List<CharacterWithRole>) : TitleCharactersUiState()
}
package ru.vladsaybulin.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.feature.details.model.asDetails
import ru.vladsaybulin.feature.details.model.asSimilarEntry
import ru.vladsaybulin.feature.details.navigation.DetailsArgs
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.EntryType
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    animeRepository: Provider<AnimeRepository>,
    userRateRepository: UserRateRepository
) : ViewModel() {

    private val args = DetailsArgs(savedStateHandle)

    val uiState = when (args.entryType) {
        EntryType.Anime ->
            combine(
                animeRepository.get().getAnimeDetails(args.entryId),
                userRateRepository.getAnimeUserRate(args.entryId),
                animeRepository.get().getSimilarAnimes(args.entryId)
            ) { details, userRate, similar ->
                DetailsUiState.Success(
                    details = details.asDetails(),
                    userRate = userRate,
                    similar = similar.map(Anime::asSimilarEntry)
                )
            }

        else -> flowOf(DetailsUiState.Error(UnsupportedOperationException()))
    }
        .catch { emit(DetailsUiState.Error(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailsUiState.Loading
        )
}

package ru.vladsaybulin.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.CreateUserRateUseCase
import ru.vladsaybulin.core.domain.GetEnableAutocorrectUserRateUseCase
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.AuthRepository
import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.feature.details.navigation.EntryDetailsArgs
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeDetails
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaDetails
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val animeRepository: Lazy<AnimeRepository>,
    private val mangaRepository: Lazy<MangaRepository>,
    private val createUserRateUseCaseProvider: Provider<CreateUserRateUseCase>,
    getEnableAutocorrectUserRateUseCase: GetEnableAutocorrectUserRateUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val args = EntryDetailsArgs(savedStateHandle)

    val enabledAutocorrectStatus = getEnableAutocorrectUserRateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    val uiState = when (args.entryType) {
        EntryType.Anime ->  combine<AnimeDetails, List<Anime>, UserRate?, DetailsUiState>(
            animeRepository.get().getAnimeDetails(args.entryId),
            animeRepository.get().getSimilarAnimes(args.entryId),
            animeRepository.get().getAnimeDetailsUserRate(args.entryId),
            ::successAnime
        )
        EntryType.Manga ->  combine<MangaDetails, List<Manga>, UserRate?, DetailsUiState>(
            mangaRepository.get().getMangaDetails(args.entryId),
            mangaRepository.get().getSimilarMangas(args.entryId),
            mangaRepository.get().getMangaDetailsUserRate(args.entryId),
            ::successManga
        )
    }
        .catch { emit(DetailsUiState.Error(it)); it.printStackTrace() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DetailsUiState.Loading
        )

    suspend fun refresh() {
        viewModelScope.launch { internalRefresh() }.join()
    }

    fun onRetry() {
        viewModelScope.launch { internalRefresh() }
    }

    fun createUserRate(status: UserRateStatus) {
        //viewModelScope.launch {
        //    createUserRateUseCaseProvider.get().invoke(
        //        userRateStatus = status,
        //        entryDetails = entryDetails.first()
        //    )
        //}
    }

    fun isAuthorized() = authRepository.isAuthorized()

    private suspend fun internalRefresh() {
        when (args.entryType) {
            EntryType.Anime -> animeRepository.get().refreshAnimeDetails(args.entryId)
            EntryType.Manga -> mangaRepository.get().refreshMangaDetails(args.entryId)
        }
    }

}
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
import ru.vladsaybulin.core.domain.GetAnimeDetailsUseCase
import ru.vladsaybulin.core.domain.GetEnableAutocorrectUserRateUseCase
import ru.vladsaybulin.core.domain.GetMangaDetailsUseCase
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.AuthRepository
import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.feature.details.navigation.EntryDetailsArgs
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeDetails
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaDetails
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateValues
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAnimeDetailsUseCase: Lazy<GetAnimeDetailsUseCase>,
    private val animeRepository: Lazy<AnimeRepository>,
    getMangaDetailsUseCase: Lazy<GetMangaDetailsUseCase>,
    private val mangaRepository: Lazy<MangaRepository>,
    private val userRateRepository: Lazy<UserRateRepository>,
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
        EntryType.Anime -> combine<AnimeDetails, List<Anime>, UserRate?, DetailsUiState>(
            getAnimeDetailsUseCase.get().invoke(args.entryId),
            animeRepository.get().getSimilarAnimes(args.entryId),
            userRateRepository.get().getAnimeUserRate(args.entryId),
            ::successAnime
        )
        EntryType.Manga -> combine<MangaDetails, List<Manga>, UserRate?, DetailsUiState>(
            getMangaDetailsUseCase.get().invoke(args.entryId),
            mangaRepository.get().getSimilarMangas(args.entryId),
            userRateRepository.get().getMangaUserRate(args.entryId),
            ::successManga
        )
    }
        .catch { emit(DetailsUiState.Error(it)); it.printStackTrace() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DetailsUiState.Loading
        )

    suspend fun refresh() {
        viewModelScope.launch { internalRefresh() }.join()
    }

    fun onRetry() {
        viewModelScope.launch { internalRefresh() }
    }

    fun createUserRate(status: UserRateStatus) {
        viewModelScope.launch {
            userRateRepository.get().createUserRate(
                entryType = args.entryType,
                entryId = args.entryId,
                userRateValues = UserRateValues(status = status)
            )
        }
    }

    fun isAuthorized() = authRepository.authState.value == ShikimoriAuthState.LOGGED_IN

    private suspend fun internalRefresh() {
        when (args.entryType) {
            EntryType.Anime -> animeRepository.get().refreshAnimeDetails(args.entryId)
            EntryType.Manga -> mangaRepository.get().refreshMangaDetails(args.entryId)
        }
    }

}
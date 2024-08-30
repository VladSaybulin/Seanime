package ru.vladsaybulin.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.GetEnableAutocorrectUserRateUseCase
import ru.vladsaybulin.core.domain.titledetails.GetFirstAnimeVideosStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.GetFirstTitleRelatedStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.RefreshTitleDetailsUseCase
import ru.vladsaybulin.core.domain.titledetails.RefreshTitleDetailsUseCase.RefreshCompleted
import ru.vladsaybulin.core.domain.titledetails.RefreshTitleDetailsUseCase.RefreshCompleted.Details
import ru.vladsaybulin.core.domain.titledetails.RefreshTitleDetailsUseCase.RefreshCompleted.Roles
import ru.vladsaybulin.core.domain.titledetails.RefreshTitleDetailsUseCase.RefreshCompleted.SkipRefresh
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.AuthRepository
import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.feature.details.navigation.toTitleDetailsScreenArgs
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateValues
import javax.inject.Inject

@HiltViewModel
class TitleDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    animeRepository: Lazy<AnimeRepository>,
    mangaRepository: Lazy<MangaRepository>,
    private val userRateRepository: UserRateRepository,
    private val refreshTitleDetailsUseCase: RefreshTitleDetailsUseCase,
    getFirstTitleRelatedStreamUseCase: GetFirstTitleRelatedStreamUseCase,
    getFirstAnimeVideosStreamUseCase: Lazy<GetFirstAnimeVideosStreamUseCase>,
    getEnableAutocorrectUserRateUseCase: GetEnableAutocorrectUserRateUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val args = savedStateHandle.toTitleDetailsScreenArgs()

    val enabledAutocorrectStatus = getEnableAutocorrectUserRateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    private val initialRefreshing = refreshTitleDetailsUseCase(args.titleType, args.titleId)
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily
        )

    val detailsState: StateFlow<TitleDetailsState> = when (args.titleType) {
        EntryType.Anime -> combine(
            animeRepository.get().getAnimeDetailsStream(args.titleId),
            getFirstTitleRelatedStreamUseCase(args.titleType, args.titleId),
            animeRepository.get().getAnimeScreenshots(args.titleId),
            getFirstAnimeVideosStreamUseCase.get().invoke(args.titleId)
        ) { details, relatedSlice, screenshots, videosSlice ->
            successTitleDetails(
                animeDetails = details,
                relatedSlice = relatedSlice,
                screenshots = screenshots,
                videosSlice = videosSlice
            )
        }

        EntryType.Manga -> combine(
            mangaRepository.get().getMangaDetailsStream(args.titleId),
            getFirstTitleRelatedStreamUseCase(args.titleType, args.titleId)
        ) { details, relatedSlice ->
            successTitleDetails(
                mangaDetails = details,
                relatedSlice = relatedSlice
            )
        }
    }
        //Await complete Details refreshing
        .onStart { initialRefreshing.first { it.equalsOrSkipped(Details) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TitleDetailsState.Loading
        )

    val rolesState: StateFlow<RolesState> = when (args.titleType) {
        EntryType.Anime -> combine(
            animeRepository.get().getAnimeMainCharactersStream(args.titleId),
            animeRepository.get().getAnimeMainAuthorsStream(args.titleId),
            RolesState::Success
        )

        EntryType.Manga -> combine(
            mangaRepository.get().getMangaMainCharactersStream(args.titleId),
            mangaRepository.get().getMangaMainAuthorsStream(args.titleId),
            RolesState::Success
        )
    }
        //Await complete Roles refreshing
        .onStart { initialRefreshing.first { it.equalsOrSkipped(Roles) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RolesState.Loading
        )

    val similarState: StateFlow<SimilarState> = when (args.titleType) {
        EntryType.Anime -> animeRepository.get().getSimilarAnimes(args.titleId)
            .map { if (it.isEmpty()) SimilarState.Empty else SimilarState.Animes(it) }

        EntryType.Manga -> mangaRepository.get().getSimilarMangasStream(args.titleId)
            .map { if (it.isEmpty()) SimilarState.Empty else SimilarState.Mangas(it) }
    }
        //Await complete Similar refreshing
        .onStart { initialRefreshing.first { it.equalsOrSkipped(Roles) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SimilarState.Loading
        )

    val userRateState = when (args.titleType) {
        EntryType.Anime -> userRateRepository.getAnimeUserRateStream(args.titleId)
        EntryType.Manga -> userRateRepository.getMangaUserRateStream(args.titleId)
    }
        .map(UserRateState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserRateState.Loading
        )

    suspend fun refresh() {
        refreshJob().join()
    }

    fun onRetry() {
        refreshJob()
    }

    fun createUserRate(status: UserRateStatus) {
        viewModelScope.launch {
            userRateRepository.createUserRate(
                entryType = args.titleType,
                entryId = args.titleId,
                userRateValues = UserRateValues(status = status)
            )
        }
    }

    fun isAuthorized() = authRepository.authState.value == ShikimoriAuthState.LOGGED_IN

    private fun refreshJob(): Job = refreshTitleDetailsUseCase(args.titleType, args.titleId, true).launchIn(viewModelScope)

}

private fun RefreshCompleted.equalsOrSkipped(refreshCompleted: RefreshCompleted) =
    this == SkipRefresh || this == refreshCompleted
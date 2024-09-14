package ru.vladsaybulin.feature.title.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.GetEnableAutocorrectUserRateUseCase
import ru.vladsaybulin.core.domain.titledetails.GetFirstAnimeVideosStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.GetFirstTitleRelatedStreamUseCase
import ru.vladsaybulin.core.domain.titledetails.GetUserRateScreamUseCase
import ru.vladsaybulin.core.domain.titledetails.UpdateTitleDetailsUseCase
import ru.vladsaybulin.core.domain.titledetails.UpdateTitleDetailsUseCase.RefreshCompleted
import ru.vladsaybulin.core.domain.titledetails.UpdateTitleDetailsUseCase.RefreshCompleted.Details
import ru.vladsaybulin.core.domain.titledetails.UpdateTitleDetailsUseCase.RefreshCompleted.Roles
import ru.vladsaybulin.core.domain.titledetails.UpdateTitleDetailsUseCase.RefreshCompleted.SkipRefresh
import ru.vladsaybulin.core.domain.titledetails.UserRateResult
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.feature.title.details.navigation.TitleDetailsScreenRoute
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
    private val updateTitleDetailsUseCase: UpdateTitleDetailsUseCase,
    getFirstTitleRelatedStreamUseCase: GetFirstTitleRelatedStreamUseCase,
    getFirstAnimeVideosStreamUseCase: Lazy<GetFirstAnimeVideosStreamUseCase>,
    getEnableAutocorrectUserRateUseCase: GetEnableAutocorrectUserRateUseCase,
    getUserRateScreamUseCase: GetUserRateScreamUseCase
) : ViewModel() {

    private val route = savedStateHandle.toRoute<TitleDetailsScreenRoute>()

    val enabledAutocorrectStatus = getEnableAutocorrectUserRateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    private val initialRefreshing = updateTitleDetailsUseCase(route.titleType, route.titleId)
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily
        )

    val detailsState: StateFlow<TitleDetailsState> = when (route.titleType) {
        EntryType.Anime -> combine(
            animeRepository.get().getAnimeDetailsStream(route.titleId),
            getFirstTitleRelatedStreamUseCase(route.titleType, route.titleId),
            animeRepository.get().getAnimeScreenshots(route.titleId),
            getFirstAnimeVideosStreamUseCase.get().invoke(route.titleId)
        ) { details, relatedSlice, screenshots, videosSlice ->
            successTitleDetails(
                animeDetails = details,
                relatedSlice = relatedSlice,
                screenshots = screenshots,
                videosSlice = videosSlice
            )
        }

        EntryType.Manga -> combine(
            mangaRepository.get().getMangaDetailsStream(route.titleId),
            getFirstTitleRelatedStreamUseCase(route.titleType, route.titleId)
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

    val rolesState: StateFlow<RolesState> = when (route.titleType) {
        EntryType.Anime -> combine(
            animeRepository.get().getAnimeMainCharactersStream(route.titleId),
            animeRepository.get().getAnimeMainAuthorsStream(route.titleId),
            RolesState::Success
        )

        EntryType.Manga -> combine(
            mangaRepository.get().getMangaMainCharactersStream(route.titleId),
            mangaRepository.get().getMangaMainAuthorsStream(route.titleId),
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

    val similarState: StateFlow<SimilarState> = when (route.titleType) {
        EntryType.Anime -> animeRepository.get().getSimilarAnimes(route.titleId)
            .map { if (it.isEmpty()) SimilarState.Empty else SimilarState.Animes(it) }

        EntryType.Manga -> mangaRepository.get().getSimilarMangasStream(route.titleId)
            .map { if (it.isEmpty()) SimilarState.Empty else SimilarState.Mangas(it) }
    }
        //Await complete Similar refreshing
        .onStart { initialRefreshing.first { it.equalsOrSkipped(Roles) } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SimilarState.Loading
        )

    val userRateState = getUserRateScreamUseCase(route.titleType, route.titleId).map {
        when (it) {
            UserRateResult.NotAuthorized -> UserRateState.NotAuthorized
            is UserRateResult.Success -> it.userRate?.let(UserRateState::Success) ?: UserRateState.NoUserRate
        }
    }.stateIn(
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
                entryType = route.titleType,
                entryId = route.titleId,
                userRateValues = UserRateValues(status = status)
            )
        }
    }

    private fun refreshJob(): Job =
        updateTitleDetailsUseCase(route.titleType, route.titleId, true).launchIn(viewModelScope)

}

private fun RefreshCompleted.equalsOrSkipped(refreshCompleted: RefreshCompleted) =
    this == SkipRefresh || this == refreshCompleted
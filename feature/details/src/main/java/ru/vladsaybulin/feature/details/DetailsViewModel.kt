package ru.vladsaybulin.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.CreateUserRateUseCase
import ru.vladsaybulin.core.domain.GetEnableAutocorrectUserRateUseCase
import ru.vladsaybulin.core.domain.GetEntryDetailsUseCase
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.feature.details.navigation.DetailsArgs
import ru.vladsaybulin.feature.userrate.Limit
import ru.vladsaybulin.feature.userrate.UserRateEditorContext
import ru.vladsaybulin.model.AnimeDetails
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.model.EntryStatus.Ongoing
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.MangaDetails
import ru.vladsaybulin.model.UserRateStatus
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userRateRepository: UserRateRepository,
    private val getEntryDetailsUseCase: GetEntryDetailsUseCase,
    private val createUserRateUseCaseProvider: Provider<CreateUserRateUseCase>,
    getEnableAutocorrectUserRateUseCase: GetEnableAutocorrectUserRateUseCase,
) : ViewModel() {

    private val args = DetailsArgs(savedStateHandle)

    private val entryDetails = MutableSharedFlow<EntryDetails>(replay = 1)

    val enabledAutocorrectStatus = getEnableAutocorrectUserRateUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = false
        )

    val userRate = when (args.entryType) {
        EntryType.Anime -> userRateRepository.getAnimeUserRate(args.entryId)
        else -> userRateRepository.getMangaUserRate(args.entryId)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    val uiState = entryDetails
        .map { details -> details.toUiState() }
        .catch { emit(DetailsUiState.Error(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DetailsUiState.Loading
        )

    init {
        refresh()
    }

    suspend fun onRefresh() {
        refresh().join()
    }

    fun onRetry() {
        refresh()
    }

    fun getUserRateEditorContext(): UserRateEditorContext? {
        val lastEntryDetailsLoaded = entryDetails.replayCache.firstOrNull() ?: return null
        if (userRate.value == null) return null
        return if (lastEntryDetailsLoaded.anime != null) {
            lastEntryDetailsLoaded.anime!!.getUserRateEditorContext()
        } else lastEntryDetailsLoaded.manga!!.getUserRateEditorContext()
    }

    fun createUserRate(status: UserRateStatus) {
        viewModelScope.launch {
            createUserRateUseCaseProvider.get().invoke(
                userRateStatus = status,
                entryDetails = entryDetails.first()
            )
        }
    }

    private fun refresh() = viewModelScope.launch {
        getEntryDetailsUseCase(args.entryType, args.entryId).collect{
            entryDetails.emit(it)
        }
    }
}

private fun AnimeDetails.getUserRateEditorContext() = UserRateEditorContext(
    entryType = EntryType.Anime,
    entryStatus = status,
    episodesLimit = when {
        episodes == 1 -> null // Нет необходимости в счётчике
        status == Ongoing && episodesAired > 0 -> Limit.Limited(episodesAired)
        episodes > 0 -> Limit.Limited(episodes)
        else -> Limit.Unlimited
    },
    chaptersLimit = null,
    volumesLimit = null
)

private fun MangaDetails.getUserRateEditorContext() = UserRateEditorContext(
    entryType = EntryType.Manga,
    entryStatus = status,
    episodesLimit = null,
    chaptersLimit = when {
        chapters > 0 -> Limit.Limited(chapters)
        else -> Limit.Unlimited
    },
    volumesLimit = when {
        volumes > 0 -> Limit.Limited(volumes)
        else -> Limit.Unlimited
    }
)
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
import ru.vladsaybulin.core.domain.GetEntryDetailsUseCase
import ru.vladsaybulin.feature.details.navigation.DetailsArgs
import ru.vladsaybulin.feature.userrate.Limit
import ru.vladsaybulin.feature.userrate.UserRateEditorContext
import ru.vladsaybulin.model.AnimeDetails
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.model.EntryStatus.Ongoing
import ru.vladsaybulin.model.EntryType
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val getEntryDetailsUseCase: GetEntryDetailsUseCase
) : ViewModel() {

    private val args = DetailsArgs(savedStateHandle)

    private val entryDetails = MutableSharedFlow<EntryDetails>(replay = 1)

    val uiState = entryDetails
        .map { details ->
            DetailsUiState(
                details.anime!!,
                details.userRate,
                details.similarEntries
            ) as DetailsUiState
        }
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
        if (lastEntryDetailsLoaded.userRate == null) return null
        return if (lastEntryDetailsLoaded.anime != null) {
            lastEntryDetailsLoaded.anime!!.getUserRateEditorContext()
        } else TODO() //Manga
    }

    private fun refresh() = viewModelScope.launch {
        entryDetails.emit(getEntryDetailsUseCase(args.entryType, args.entryId).first())
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
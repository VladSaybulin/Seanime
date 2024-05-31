package ru.vladsaybulin.feature.userrate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.EditableUserRate
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateStatus.Completed
import ru.vladsaybulin.model.userrate.UserRateStatus.Dropped
import ru.vladsaybulin.model.userrate.UserRateStatus.OnHold
import ru.vladsaybulin.model.userrate.UserRateStatus.Planned
import ru.vladsaybulin.model.userrate.UserRateStatus.Rewatching
import ru.vladsaybulin.model.userrate.UserRateStatus.Watching
import ru.vladsaybulin.model.userrate.UserRateValues
import javax.inject.Inject

@HiltViewModel
class UserRateViewModel @Inject constructor(
    private val userRateRepository: UserRateRepository
) : ViewModel() {

    private var userRateId: Long? = null

    private val editableUserRateFlow = MutableStateFlow<EditableUserRate?>(null)
    val uiState = editableUserRateFlow 
        .map { editableUserRate ->
            if (editableUserRate == null) {
                UserRateUiState.Hided
            } else {
                userRateId = editableUserRate.userRate.id
                UserRateUiState.Show(
                    entryType = editableUserRate.titleType,
                    entryStatus = editableUserRate.entryStatus,
                    initialUserRate = editableUserRate.userRate.asValues(),
                    episodesLimit = editableUserRate.maxEpisodes,
                    chaptersLimit = editableUserRate.maxChapters,
                    volumesLimit = editableUserRate.maxVolumes,
                    availableUserRateStatuses = getAvailableUserRateStatuses(editableUserRate.entryStatus)
                        .takeIf { editableUserRate.userRate.status in it }
                        ?: AllUserRateStatuses
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserRateUiState.Hided
        )


    fun show(editableUserRate: EditableUserRate) {
        editableUserRateFlow.value = editableUserRate
    }

    fun hide() {
        editableUserRateFlow.value = null
    }

    fun save(userRateValues: UserRateValues) {
        val userRateId = userRateId ?: return
        viewModelScope.launch {
            userRateRepository.updateUserRate(userRateId, userRateValues)
        }
    }

    fun delete() {
        val userRateId = userRateId ?: return
        viewModelScope.launch {
            userRateRepository.deleteUserRate(userRateId)
        }
    }
}

private fun UserRate.asValues() = UserRateValues(
    status = status,
    score = score,
    episodes = episodes,
    chapters = chapters,
    volumes = volumes,
    rewatches = rewatches,
    text = text
)

private fun getAvailableUserRateStatuses(entryStatus: EntryStatus) = buildList {
    add(Planned)
    if (entryStatus != EntryStatus.Anons) {
        add(Watching)
        add(Rewatching)
    }
    if (entryStatus == EntryStatus.Released || entryStatus == EntryStatus.Discontinued) {
        add(Completed)
    }
    add(OnHold)
    add(Dropped)
}

private val AllUserRateStatuses = listOf(Planned, Watching, Rewatching, Completed, OnHold, Dropped)

sealed class UserRateUiState {

    data object Hided : UserRateUiState()

    data class Show(
        val entryType: EntryType,
        val entryStatus: EntryStatus,
        val initialUserRate: UserRateValues,
        val availableUserRateStatuses: List<UserRateStatus>,
        val episodesLimit: Int,
        val chaptersLimit: Int,
        val volumesLimit: Int
    ) : UserRateUiState()
}
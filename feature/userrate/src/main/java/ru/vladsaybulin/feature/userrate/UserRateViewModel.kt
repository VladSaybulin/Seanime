package ru.vladsaybulin.feature.userrate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vladsaybulin.core.domain.GetEnableAutocorrectUserRateUseCase
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Manga
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.UserRateValues
import ru.vladsaybulin.model.UserRateWithEntry
import javax.inject.Inject

@HiltViewModel
class UserRateViewModel @Inject constructor(
    getEnabledAutocorrectUseCase: GetEnableAutocorrectUserRateUseCase,
    private val userRateRepository: UserRateRepository
) : ViewModel() {

    private val userRateWithContext =
        MutableSharedFlow<Pair<UserRate, UserRateEditorContext>>(replay = 1)

    val setup = combine(
        getEnabledAutocorrectUseCase(),
        userRateWithContext
    ) { enabledAutocorrect, (userRate, context) ->
        UserRateSetup.Edit(
            userRate = userRate,
            context = context,
            enabledAutocorrect = enabledAutocorrect
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UserRateSetup.None
        )

    fun setUserRate(userRateWithEntry: UserRateWithEntry) {
        userRateWithContext.tryEmit(userRateWithEntry.userRate to (userRateWithEntry.anime?.context ?: userRateWithEntry.manga!!.context))
    }

    fun setupUserRate(userRate: UserRate, context: UserRateEditorContext) {
        userRateWithContext.tryEmit(userRate to context)
    }

    fun save(userRateValues: UserRateValues) {
        val userRateId = getUserRateId() ?: return
        viewModelScope.launch {
            userRateRepository.updateUserRate(userRateId, userRateValues)
        }
    }

    fun delete() {
        val userRateId = getUserRateId() ?: return
        viewModelScope.launch {
            userRateRepository.deleteUserRate(userRateId)
        }
    }

    private fun getUserRateId(): Long? {
        val currSetup = setup.value
        return if (currSetup is UserRateSetup.Edit) {
            currSetup.userRate.id
        } else null
    }
}

val Anime.context
    get() = UserRateEditorContext(
        entryType = EntryType.Anime,
        entryStatus = status,
        episodesLimit = Limit.Unlimited
    )


val Manga.context
    get() = UserRateEditorContext(
        entryType = EntryType.Manga,
        entryStatus = status,
        chaptersLimit = Limit.Unlimited,
        volumesLimit = Limit.Unlimited
    )
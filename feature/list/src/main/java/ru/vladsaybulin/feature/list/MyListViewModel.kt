package ru.vladsaybulin.feature.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import ru.vladsaybulin.core.domain.GetPagedUserRatesUseCase
import ru.vladsaybulin.data.repository.AuthRepository
import ru.vladsaybulin.feature.list.navigation.ListScreenRoute
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrderField
import ru.vladsaybulin.model.userrate.UserRateStatus
import javax.inject.Inject

@HiltViewModel
class MyListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getPagedUserRatesUseCase: GetPagedUserRatesUseCase,
    authRepository: AuthRepository,
) : ViewModel() {

    private val args = savedStateHandle.toRoute<ListScreenRoute>()

    private val authState = authRepository.authState

    private val controlPanel = MutableStateFlow(
        ListControlPanelState(
            entryType = args.titleType,
            userRateStatus = args.status,
            orderField = UserRateOrderField.CreatedAt,
            order = UserRateOrder.Asc
        )
    )

    private val pagedUserRates = authState.flatMapLatest { currentAuthState ->
        if (currentAuthState == ShikimoriAuthState.LOGGED_OUT) {
            flowOf(PagingData.empty())
        } else {
            controlPanel.flatMapLatest { (type, status, field, order) ->
                getPagedUserRatesUseCase(type, status, field, order)
            }
        }
    }.cachedIn(viewModelScope)

    internal val screenState = authState.flatMapLatest { currentAuthState ->
        if (currentAuthState == ShikimoriAuthState.LOGGED_OUT) {
            flowOf<ListScreenState>(ListScreenState.LoggedOut)
        } else {
            controlPanel.map { controlPanelState ->
                ListScreenState.Success(
                    data = pagedUserRates,
                    controlPanelState = controlPanelState
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ListScreenState.Loading
    )


    fun onEntryTypeChanged(entryType: EntryType) {
        controlPanel.update { it.copy(entryType = entryType) }
    }

    fun onUserRateStatusChanged(userRateStatus: UserRateStatus) {
        controlPanel.update { it.copy(userRateStatus = userRateStatus) }
    }
}
package ru.vladsaybulin.feature.list

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.model.userrate.UserRateWithEntry

@Immutable
internal sealed class ListScreenState {

    data object Loading : ListScreenState()

    data object LoggedOut : ListScreenState()

    data class Success(
        val controlPanelState: ListControlPanelState,
        val data: Flow<PagingData<UserRateWithEntry>>,
    ) : ListScreenState()

}
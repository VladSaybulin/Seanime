package ru.vladsaybulin.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import ru.vladsaybulin.core.designsystem.components.ShikimoriDropdownChip
import ru.vladsaybulin.core.navigation.args.EntryDetailsArgs
import ru.vladsaybulin.core.ui.userrate.UserRateEntryCard
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateWithEntry

@Composable
fun MyListRoute(
    onEntryClick: (EntryDetailsArgs) -> Unit,
    viewModel: MyListViewModel = hiltViewModel()
) {

    val entryType by viewModel.entryType.collectAsStateWithLifecycle()
    val userRateStatus by viewModel.userRateStatus.collectAsStateWithLifecycle()
    val userRates = viewModel.userRatesPagingData.collectAsLazyPagingItems()

    MyListScreen(
        userRates = userRates,
        entryType = entryType,
        onEntryTypeChange = viewModel::onEntryTypeChanged,
        userRateStatus = userRateStatus,
        onUserRateStatusChange = viewModel::onUserRateStatusChanged,
        onEntryClick = onEntryClick
    )
}

@Composable
fun MyListScreen(
    userRates: LazyPagingItems<UserRateWithEntry>,
    entryType: EntryType,
    onEntryTypeChange: (EntryType) -> Unit,
    userRateStatus: UserRateStatus,
    onUserRateStatusChange: (UserRateStatus) -> Unit,
    onEntryClick: (EntryDetailsArgs) -> Unit
) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .padding(bottom = 80.dp)
    ) {
        val loadState = userRates.loadState.refresh
        if (loadState is LoadState.Error) {
            loadState.error.printStackTrace()
        }

        Column {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ShikimoriDropdownChip(
                    items = listOf(EntryType.Anime, EntryType.Manga),
                    onItemClick = onEntryTypeChange,
                    selected = true,
                    selectedLabel = { Text(entryType.name) },
                    itemLabel = { Text(it.name) }
                )

                ShikimoriDropdownChip(
                    items = UserRateStatus.entries.filter { it != UserRateStatus.None },
                    onItemClick = onUserRateStatusChange,
                    selected = true,
                    selectedLabel = { Text(userRateStatus.name) },
                    itemLabel = { Text(it.name) }
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(
                    count = userRates.itemCount,
                    key = userRates.itemKey { it.anime?.id ?: it.manga!!.id }
                ) {
                    userRates[it]?.let {
                        UserRateEntryCard(
                            userRateWithEntry = it,
                            onClick = { type, id -> onEntryClick(EntryDetailsArgs(type, id)) },
                            showUserRateBadge = false
                        )
                    }
                }
            }
        }
    }
}
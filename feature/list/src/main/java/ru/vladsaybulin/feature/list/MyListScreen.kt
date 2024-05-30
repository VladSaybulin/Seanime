package ru.vladsaybulin.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ru.vladsaybulin.core.designsystem.components.ShikimoriDropdownChip
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.navigation.SeanimeNavigator
import ru.vladsaybulin.core.navigation.animeDetails
import ru.vladsaybulin.core.navigation.mangaDetails
import ru.vladsaybulin.core.ui.ErrorMessageColumn
import ru.vladsaybulin.core.ui.LazyPagingColumn
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.strings.LocalTargetStringsEntry
import ru.vladsaybulin.core.ui.strings.asTargetStringEntry
import ru.vladsaybulin.core.ui.strings.entryTypeString
import ru.vladsaybulin.core.ui.strings.userRateStatusString
import ru.vladsaybulin.core.ui.userrate.UserRateEntryCard
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrder.Asc
import ru.vladsaybulin.model.list.UserRateOrder.Desc
import ru.vladsaybulin.model.list.UserRateOrderField
import ru.vladsaybulin.model.list.UserRateOrderField.CreatedAt
import ru.vladsaybulin.model.list.UserRateOrderField.UpdatedAt
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateWithEntry

@Composable
fun MyListRoute(
    navigator: SeanimeNavigator,
    viewModel: MyListViewModel = hiltViewModel()
) {

    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    MyListScreen(
        screenState = screenState,
        onEntryTypeChange = viewModel::onEntryTypeChanged,
        onUserRateStatusChange = viewModel::onUserRateStatusChanged,
        onOrderFieldChange = viewModel::onOrderFieldChange,
        onOrderChange = viewModel::onOrderChange,
        navigator = navigator
    )
}

@Composable
internal fun MyListScreen(
    screenState: ListScreenState,
    onEntryTypeChange: (EntryType) -> Unit,
    onUserRateStatusChange: (UserRateStatus) -> Unit,
    onOrderFieldChange: (UserRateOrderField) -> Unit,
    onOrderChange: (UserRateOrder) -> Unit,
    navigator: SeanimeNavigator,
) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .padding(LocalScreenContentPadding.current)
            .fillMaxSize()
    ) {
        when (screenState) {
            ListScreenState.Loading ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

            ListScreenState.LoggedOut ->
                Authorization(onSignIn = navigator::auth)

            is ListScreenState.Success -> ListContent(
                state = screenState,
                onEntryTypeChange = onEntryTypeChange,
                onUserRateStatusChange = onUserRateStatusChange,
                onOrderFieldChange = onOrderFieldChange,
                onOrderChange = onOrderChange,
                navigator = navigator
            )
        }
    }
}

@Composable
private fun ListContent(
    state: ListScreenState.Success,
    onEntryTypeChange: (EntryType) -> Unit,
    onUserRateStatusChange: (UserRateStatus) -> Unit,
    onOrderFieldChange: (UserRateOrderField) -> Unit,
    onOrderChange: (UserRateOrder) -> Unit,
    navigator: SeanimeNavigator,
) {
    CompositionLocalProvider(value = LocalTargetStringsEntry provides state.controlPanelState.entryType.asTargetStringEntry()) {
        Column {
            ControlPanel(
                entryType = state.controlPanelState.entryType,
                userRateStatus = state.controlPanelState.userRateStatus,
                orderField = state.controlPanelState.orderField,
                order = state.controlPanelState.order,
                onEntryTypeChange = onEntryTypeChange,
                onUserRateStatusChange = onUserRateStatusChange,
                onOrderChange = onOrderChange,
                onOrderFieldChange = onOrderFieldChange
            )

            val userRates = state.data.collectAsLazyPagingItems()
            UserRatesPaging(
                userRates = userRates,
                onAnimeClick = navigator::animeDetails,
                onMangaClick = navigator::mangaDetails,
                onEditClick = navigator::userRate
            )
        }
    }
}

@Composable
private fun AuthorizationError(onSignIn: () -> Unit) {
    ErrorMessageColumn(
        header = { Text(stringResource(id = R.string.feature_list_authorization_error)) },
        description = { Text(stringResource(id = R.string.feature_list_authorization_error_description)) },
        action = {
            Button(onClick = onSignIn) {
                Text(text = stringResource(id = R.string.feature_list_sign_in))
            }
        }
    )
}

@Composable
private fun Authorization(onSignIn: () -> Unit) {
    ErrorMessageColumn(
        header = { Text(stringResource(id = R.string.feature_list_authorization)) },
        description = { Text(stringResource(id = R.string.feature_list_authorization_description)) },
        action = {
            Button(onClick = onSignIn) {
                Text(text = stringResource(id = R.string.feature_list_sign_in))
            }
        }
    )
}

@Composable
private fun ControlPanel(
    entryType: EntryType,
    userRateStatus: UserRateStatus,
    orderField: UserRateOrderField,
    order: UserRateOrder,
    onEntryTypeChange: (EntryType) -> Unit,
    onUserRateStatusChange: (UserRateStatus) -> Unit,
    onOrderFieldChange: (UserRateOrderField) -> Unit,
    onOrderChange: (UserRateOrder) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            ShikimoriDropdownChip(
                items = listOf(EntryType.Anime, EntryType.Manga),
                onItemClick = onEntryTypeChange,
                selected = true,
                selectedLabel = { Text(entryTypeString(entryType = entryType)) },
                itemLabel = { Text(entryTypeString(entryType = it)) }
            )
        }

        item {
            ShikimoriDropdownChip(
                items = UserRateStatus.entries.filter { it != UserRateStatus.None },
                onItemClick = onUserRateStatusChange,
                selected = true,
                selectedLabel = { Text(userRateStatusString(userRateStatus)) },
                itemLabel = { Text(userRateStatusString(it)) }
            )
        }

        item {
            ShikimoriDropdownChip(
                items = UserRateOrderField.entries,
                onItemClick = onOrderFieldChange,
                selected = true,
                selectedLabel = { Text(userRateOrderFieldString(orderField)) },
                itemLabel = { Text(userRateOrderFieldString(it)) }
            )
        }

        item {
            InputChip(
                selected = true,
                onClick = {
                    onOrderChange(
                        when (order) {
                            Asc -> Desc
                            Desc -> Asc
                        }
                    )
                },
                label = {
                    Icon(
                        imageVector = when (order) {
                            Asc -> SeanimeIcons.ArrowDownward
                            Desc -> SeanimeIcons.ArrowUpward
                        },
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun UserRatesPaging(
    userRates: LazyPagingItems<UserRateWithEntry>,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    onEditClick: (UserRateWithEntry) -> Unit
) {
    LazyPagingColumn(
        lazyPagingItems = userRates,
        itemKey = { it.userRate.id },
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        UserRateEntryCard(
            userRateWithEntry = it,
            onAnimeClick = onAnimeClick,
            onMangaClick = onMangaClick,
            showUserRateBadge = false,
            onEditClick = { onEditClick(it) }
        )
    }
}

@Composable
fun userRateOrderFieldString(orderField: UserRateOrderField) = stringResource(
    id = when (orderField) {
        CreatedAt -> R.string.feature_list_user_rate_order_field_created_at
        UpdatedAt -> R.string.feature_list_user_rate_order_field_updated_at
    }
)

@Composable
@Preview
fun AuthorizationErrorPreview() {
    SeanimeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AuthorizationError(onSignIn = { })
        }
    }
}

@Composable
@Preview
fun AuthorizationScreenPreview() {
    SeanimeTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Authorization(onSignIn = { })
        }
    }
}
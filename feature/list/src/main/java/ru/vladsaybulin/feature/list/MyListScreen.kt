package ru.vladsaybulin.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ru.vladsaybulin.core.designsystem.components.ShikimoriDropdownChip
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.navigation.args.EntryDetailsArgs
import ru.vladsaybulin.core.ui.ErrorMessageColumn
import ru.vladsaybulin.core.ui.LazyPagingColumn
import ru.vladsaybulin.core.ui.strings.LocalTargetStringsEntry
import ru.vladsaybulin.core.ui.strings.asTargetStringEntry
import ru.vladsaybulin.core.ui.strings.entryTypeString
import ru.vladsaybulin.core.ui.strings.userRateStatusString
import ru.vladsaybulin.core.ui.userrate.UserRateEntryCard
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateWithEntry

@Composable
fun MyListRoute(
    onEntryClick: (EntryDetailsArgs) -> Unit,
    onSignIn: () -> Unit,
    viewModel: MyListViewModel = hiltViewModel()
) {

    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userRates = viewModel.userRatesPagingData.collectAsLazyPagingItems()

    MyListScreen(
        authState = authState,
        uiState = uiState,
        userRates = userRates,
        onEntryTypeChange = viewModel::onEntryTypeChanged,
        onUserRateStatusChange = viewModel::onUserRateStatusChanged,
        onEntryClick = onEntryClick,
        onSignIn = onSignIn
    )
}

@Composable
fun MyListScreen(
    authState: ShikimoriAuthState,
    uiState: ListUiState,
    userRates: LazyPagingItems<UserRateWithEntry>,
    onEntryTypeChange: (EntryType) -> Unit,
    onUserRateStatusChange: (UserRateStatus) -> Unit,
    onEntryClick: (EntryDetailsArgs) -> Unit,
    onSignIn: () -> Unit
) {
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .padding(bottom = 80.dp)
    ) {
        when (authState) {
            is ShikimoriAuthState.Error -> AuthorizationError(onSignIn = onSignIn)
            ShikimoriAuthState.NotAuthorized -> Authorization(onSignIn = onSignIn)
            ShikimoriAuthState.Authorized -> ListContent(
                uiState = uiState,
                userRates = userRates,
                onEntryTypeChange = onEntryTypeChange,
                onUserRateStatusChange = onUserRateStatusChange,
                onEntryClick = onEntryClick
            )
        }
    }
}

@Composable
private fun ListContent(
    uiState: ListUiState,
    userRates: LazyPagingItems<UserRateWithEntry>,
    onEntryTypeChange: (EntryType) -> Unit,
    onUserRateStatusChange: (UserRateStatus) -> Unit,
    onEntryClick: (EntryDetailsArgs) -> Unit,
) {
    CompositionLocalProvider(value = LocalTargetStringsEntry provides uiState.entryType.asTargetStringEntry()) {
        Column {
            ControlPanel(
                entryType = uiState.entryType,
                userRateStatus = uiState.userRateStatus,
                onEntryTypeChange = onEntryTypeChange,
                onUserRateStatusChange = onUserRateStatusChange
            )

            UserRatesPaging(
                userRates = userRates,
                onEntryClick = onEntryClick
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
    onEntryTypeChange: (EntryType) -> Unit,
    onUserRateStatusChange: (UserRateStatus) -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ShikimoriDropdownChip(
            items = listOf(EntryType.Anime, EntryType.Manga),
            onItemClick = onEntryTypeChange,
            selected = true,
            selectedLabel = { Text(entryTypeString(entryType = entryType)) },
            itemLabel = { Text(entryTypeString(entryType = it)) }
        )

        ShikimoriDropdownChip(
            items = UserRateStatus.entries.filter { it != UserRateStatus.None },
            onItemClick = onUserRateStatusChange,
            selected = true,
            selectedLabel = { Text(userRateStatusString(userRateStatus)) },
            itemLabel = { Text(userRateStatusString(it)) }
        )
    }
}

@Composable
private fun UserRatesPaging(
    userRates: LazyPagingItems<UserRateWithEntry>,
    onEntryClick: (EntryDetailsArgs) -> Unit
) {
    LazyPagingColumn(
        lazyPagingItems = userRates,
        itemKey = { it.userRate.id },
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        UserRateEntryCard(
            userRateWithEntry = it,
            onClick = { type, id -> onEntryClick(EntryDetailsArgs(type, id)) },
            showUserRateBadge = false
        )
    }
}

@Composable
@Preview
fun AuthorizationErrorPreview() {
    ShikimoriTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AuthorizationError(onSignIn = { })
        }
    }
}

@Composable
@Preview
fun AuthorizationScreenPreview() {
    ShikimoriTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Authorization(onSignIn = { })
        }
    }
}
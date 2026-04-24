package ru.vladsaybulin.feature.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import ru.vladsaybulin.core.designsystem.components.ShikimoriDropdownChip
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.ErrorMessageColumn
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.userrate.UserRateEntryCard
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.core.ui2.strings.compose.asString
import ru.vladsaybulin.core.ui2.entry.EntryList
import ru.vladsaybulin.core.ui2.entry.userrate.UserRateItem
import ru.vladsaybulin.feature.list.navigation.ListNavEvents
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.userrate.EditableUserRate
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateWithEntry

@Composable
fun ListScreen(
    navEvents: ListNavEvents,
    viewModel: MyListViewModel = hiltViewModel()
) {

    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    MyListScreen(
        screenState = screenState,
        onEntryTypeChange = viewModel::onEntryTypeChanged,
        onUserRateStatusChange = viewModel::onUserRateStatusChanged,
        onAnimeClick = { navEvents.navigateToTitleDetails(EntryType.Anime, it.id) },
        onMangaClick = { navEvents.navigateToTitleDetails(EntryType.Manga, it.id) },
        onAuthorization = navEvents.startAuthorization,
        onEditClick = navEvents.showUserRateEditor
    )
}

@Composable
internal fun MyListScreen(
    screenState: ListScreenState,
    onEntryTypeChange: (EntryType) -> Unit,
    onUserRateStatusChange: (UserRateStatus) -> Unit,
    onAuthorization: () -> Unit,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    onEditClick: (EditableUserRate) -> Unit,
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

            ListScreenState.LoggedOut -> Authorization(onSignIn = onAuthorization)

            is ListScreenState.Success -> ListContent(
                state = screenState,
                onEntryTypeChange = onEntryTypeChange,
                onUserRateStatusChange = onUserRateStatusChange,
                onAnimeClick = onAnimeClick,
                onMangaClick = onMangaClick,
                onEditClick = onEditClick
            )
        }
    }
}

@Composable
private fun ListContent(
    state: ListScreenState.Success,
    onEntryTypeChange: (EntryType) -> Unit,
    onUserRateStatusChange: (UserRateStatus) -> Unit,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    onEditClick: (EditableUserRate) -> Unit
) {
    ProvideTitleStringsByType(titleType = state.controlPanelState.entryType) {
        Column {
            ControlPanel(
                entryType = state.controlPanelState.entryType,
                userRateStatus = state.controlPanelState.userRateStatus,
                onEntryTypeChange = onEntryTypeChange,
                onUserRateStatusChange = onUserRateStatusChange
            )

            val userRates = state.data.collectAsLazyPagingItems()
            UserRatesPaging(
                userRates = userRates,
                onAnimeClick = onAnimeClick,
                onMangaClick = onMangaClick,
                onEditClick = onEditClick
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
            selectedLabel = { Text(entryType.asString()) },
            itemLabel = { Text(it.asString()) }
        )

        ShikimoriDropdownChip(
            items = UserRateStatus.entries.filter { it != UserRateStatus.None },
            onItemClick = onUserRateStatusChange,
            selected = true,
            selectedLabel = { Text(userRateStatus.asString()) },
            itemLabel = { Text(it.asString()) }
        )
    }
}

@Composable
private fun UserRatesPaging(
    userRates: LazyPagingItems<UserRateWithEntry>,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    onEditClick: (EditableUserRate) -> Unit
) {
    EntryList {
        items(
            count = userRates.itemCount,
            key = userRates.itemKey { it.userRate.id },
            contentType = userRates.itemContentType()
        ) {
            val userRateWithEntry = userRates[it] ?: return@items
            UserRateItem(
                userRateWithEntry = userRateWithEntry,
                onAnimeClick = onAnimeClick,
                onMangaClick = onMangaClick,
                onEditClick = { }
            )
        }
    }
}

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
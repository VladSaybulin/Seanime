/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.feature.list.impl

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import ru.vladsaybulin.core.designsystem.components.ShikimoriDropdownChip
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.ErrorMessageColumn
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui2.entry.EntryList
import ru.vladsaybulin.core.ui2.entry.userrate.UserRateItem
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.core.ui2.strings.compose.asString
import ru.vladsaybulin.feature.rate.editor.api.navigation.TitleReference
import ru.vladsaybulin.feature.rate.editor.api.navigation.titleReference
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateContext
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateValues
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import ru.vladsaybulin.model.userrate.extractRateContext
import ru.vladsaybulin.model.userrate.toUserRateValues

@Composable
fun ListScreen(
    viewModel: ListViewModel,
    onAnimeClick: (Long) -> Unit,
    onMangaClick: (Long) -> Unit,
    onRateClick: (Long, TitleReference, UserRateValues, UserRateContext) -> Unit,
) {

    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    ListScreen(
        screenState = screenState,
        onEntryTypeChange = viewModel::onEntryTypeChanged,
        onUserRateStatusChange = viewModel::onUserRateStatusChanged,
        onAnimeClick = onAnimeClick,
        onLogin = { /* viewModel.login() */ },
        onMangaClick = onMangaClick,
        onRateClick = onRateClick
    )
}

@Composable
internal fun ListScreen(
    screenState: ListScreenState,
    onEntryTypeChange: (EntryType) -> Unit,
    onUserRateStatusChange: (UserRateStatus) -> Unit,
    onAnimeClick: (Long) -> Unit,
    onLogin: () -> Unit,
    onMangaClick: (Long) -> Unit,
    onRateClick: (Long, TitleReference, UserRateValues, UserRateContext) -> Unit,
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

            ListScreenState.LoggedOut -> Authorization(onSignIn = onLogin)

            is ListScreenState.Success -> ListContent(
                state = screenState,
                onEntryTypeChange = onEntryTypeChange,
                onUserRateStatusChange = onUserRateStatusChange,
                onAnimeClick = onAnimeClick,
                onMangaClick = onMangaClick,
                onRateClick = onRateClick
            )
        }
    }
}

@Composable
private fun ListContent(
    state: ListScreenState.Success,
    onEntryTypeChange: (EntryType) -> Unit,
    onUserRateStatusChange: (UserRateStatus) -> Unit,
    onAnimeClick: (Long) -> Unit,
    onMangaClick: (Long) -> Unit,
    onRateClick: (Long, TitleReference, UserRateValues, UserRateContext) -> Unit,
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
                onRateClick = onRateClick
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
    onAnimeClick: (Long) -> Unit,
    onMangaClick: (Long) -> Unit,
    onRateClick: (Long, TitleReference, UserRateValues, UserRateContext) -> Unit,
) {
    EntryList {
        items(
            count = userRates.itemCount,
            key = userRates.itemKey { it.userRate.id },
            contentType = userRates.itemContentType()
        ) { index ->
            val userRateWithEntry = userRates[index] ?: return@items
            UserRateItem(
                userRateWithEntry = userRateWithEntry,
                onAnimeClick = { onAnimeClick(it.id) },
                onMangaClick = { onMangaClick(it.id) },
                onEditClick = {
                    userRateWithEntry.anime?.let { anime ->
                        onRateClick(
                            userRateWithEntry.userRate.id,
                            anime.titleReference(),
                            userRateWithEntry.userRate.toUserRateValues(),
                            anime.extractRateContext()
                        )
                    } ?: userRateWithEntry.manga?.let { manga ->
                        onRateClick(
                            userRateWithEntry.userRate.id,
                            manga.titleReference(),
                            userRateWithEntry.userRate.toUserRateValues(),
                            manga.extractRateContext()
                        )
                    }
                }
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
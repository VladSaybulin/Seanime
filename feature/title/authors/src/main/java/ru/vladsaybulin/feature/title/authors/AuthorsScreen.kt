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

package ru.vladsaybulin.feature.title.authors

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.feature.title.authors.R
import ru.vladsaybulin.feature.title.authors.navigation.TitleAuthorsNavEvents
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.core.ui2.entry.EntryList

@Composable
fun AuthorsScreen(
    navEvents: TitleAuthorsNavEvents,
    viewModel: AuthorsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AuthorsScreen(
        uiState = uiState,
        onAuthorClick = { navEvents.navigateToPerson(it.person.id) },
        onBack = navEvents.navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthorsScreen(
    uiState: AuthorsUiState,
    onAuthorClick: (PersonWithRoles) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AuthorsTopBar(
                onBack = onBack,
                scrollBehavior = scrollBehavior
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Box(
            modifier = Modifier.padding(padding)
        ) {
            when (uiState) {
                AuthorsUiState.Loading -> Unit

                is AuthorsUiState.Success -> AuthorsContent(
                    uiState = uiState,
                    onAuthorClick = onAuthorClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthorsTopBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.feature_authors_title))
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = SeanimeIcons.ArrowBack,
                    contentDescription = stringResource(id = R.string.feature_authors_back_icon)
                )
            }
        },
        modifier = modifier,
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun AuthorsContent(
    uiState: AuthorsUiState.Success,
    onAuthorClick: (PersonWithRoles) -> Unit
) {
    EntryList {
        authors(
            authors = uiState.authors,
            onAuthorClick = onAuthorClick
        )
    }
}

private fun LazyListScope.authors(
    authors: List<PersonWithRoles>,
    onAuthorClick: (PersonWithRoles) -> Unit
) {
    items(authors) {
        AuthorItem(
            author = it,
            onClick = { onAuthorClick(it) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
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

package ru.vladsaybulin.feature.title.related

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.feature.title.related.navigation.TitleRelatedNavEvents
import ru.vladsaybulin.feature.titlerelated.R
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.core.ui2.entry.EntryList
import ru.vladsaybulin.core.ui2.entry.related.RelatedTitleItem

@Composable
fun TitleRelatedRoute(
    navEvents: TitleRelatedNavEvents,
    viewModel: TitleRelatedViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    TitleRelatedScreen(
        state = state,
        navEvents = navEvents
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleRelatedScreen(
    state: TitleRelatedUiState,
    navEvents: TitleRelatedNavEvents
) {
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.feature_titlerelated_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = navEvents.navigateUp) {
                        Icon(
                            imageVector = SeanimeIcons.ArrowBack,
                            contentDescription = stringResource(id = R.string.feature_titlerelated_back)
                        )
                    }
                },
                scrollBehavior = topBarScrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(topBarScrollBehavior.nestedScrollConnection)
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .padding(scaffoldPadding)
                .padding(LocalScreenContentPadding.current)
        ) {
            if (state is TitleRelatedUiState.Success) {
                TitleRelatedContent(state = state, onTitleClick = navEvents.navigateToTitleDetails)
            }
        }
    }
}

@Composable
private fun TitleRelatedContent(
    state: TitleRelatedUiState.Success,
    onTitleClick: (EntryType, Long) -> Unit
) {
    EntryList {
        items(items = state.relatedTitles) { relatedTitle ->
            RelatedTitleItem(
                relatedTitle = relatedTitle,
                onClick = onTitleClick
            )
        }
    }
}
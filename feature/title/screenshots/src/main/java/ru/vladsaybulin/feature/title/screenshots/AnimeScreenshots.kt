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

package ru.vladsaybulin.feature.title.screenshots

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.feature.title.screenshots.navigation.AnimeScreenshotsNavEvents
import ru.vladsaybulin.model.common.Image

@Composable
fun AnimeScreenshotsRoute(
    navEvents: AnimeScreenshotsNavEvents,
    viewModel: AnimeScreenshotsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AnimeScreenshotsScreen(
        state = state,
        navEvents = navEvents
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeScreenshotsScreen(
    state: AnimeScreenshotsUiState,
    navEvents: AnimeScreenshotsNavEvents,
) {
    val topBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            AnimeScreenshotsTopBar(
                onBackClick = navEvents.navigateUp,
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
            if (state is AnimeScreenshotsUiState.Success) {
                AnimeScreenshotsContent(
                    state = state,
                    onScreenshotClick = { navEvents.showFullscreenImage(state.screenshots, it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeScreenshotsTopBar(onBackClick: () -> Unit, scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.feature_anime_screenshots_title)) },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = SeanimeIcons.ArrowBack,
                    contentDescription = stringResource(id = R.string.feature_anime_screenshots_back)
                )
            }
        }
    )
}

@Composable
private fun AnimeScreenshotsContent(
    state: AnimeScreenshotsUiState.Success,
    onScreenshotClick: (index: Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(96.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 8.dp)
    ) {
        itemsIndexed(items = state.screenshots) { index, screenshot ->
            AnimeScreenshotCard(
                screenshot = screenshot,
                onClick = { onScreenshotClick(index) }
            )
        }
    }
}

@Composable
private fun AnimeScreenshotCard(
    screenshot: Image,
    onClick: () -> Unit
) {
    AsyncImage(
        model = screenshot.previewUrl,
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick)
    )
}
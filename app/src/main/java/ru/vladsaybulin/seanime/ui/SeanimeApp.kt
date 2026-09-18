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

package ru.vladsaybulin.seanime.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.feature.calendar.impl.navigation.calendarEntry
import ru.vladsaybulin.feature.character.impl.navigation.characterEntry
import ru.vladsaybulin.feature.home.impl.navigation.homeEntry
import ru.vladsaybulin.feature.imageview.impl.FullScreenImageState
import ru.vladsaybulin.feature.imageview.impl.FullScreenImageView
import ru.vladsaybulin.feature.imageview.impl.navigation.imageViewEntry
import ru.vladsaybulin.feature.list.impl.navigation.listEntry
import ru.vladsaybulin.feature.profile.impl.navigation.profileEntry
import ru.vladsaybulin.feature.search.impl.navigation.searchEntry
import ru.vladsaybulin.feature.title.authors.impl.navigation.titleAuthorsEntry
import ru.vladsaybulin.feature.title.characters.impl.titleCharactersEntry
import ru.vladsaybulin.feature.title.details.impl.navigation.titleDetailsEntry
import ru.vladsaybulin.feature.title.related.impl.navigator.titleRelatedEntry
import ru.vladsaybulin.feature.title.screenshots.impl.navigator.animeScreenshotsEntry
import ru.vladsaybulin.feature.title.videos.impl.navigation.animeVideosEntry
import ru.vladsaybulin.model.userrate.EditableUserRate
import ru.vladsaybulin.seanime.navigation.TopLevelDestination

@Composable
fun SeanimeApp(
    appState: SeanimeAppState
) {
    SeanimeTheme {
        val fullScreenImageState = remember { FullScreenImageState() }

        var editableUserRate by remember {
            mutableStateOf<EditableUserRate?>(null)
        }

        val navigator = appState.navState.navigator

        Scaffold(
            bottomBar = {
                if (appState.shouldShowBottomBar) {
                    SeanimeBottomBar(
                        destinations = TopLevelDestination.entries,
                        onNavigateToDestination = { navigator.navigateToTopLevel(it.navKey) },
                        currentDestination = appState.navState.currentTopLevelKey
                    )
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { padding ->
            Row(
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding)
            ) {
                if (appState.shouldShowNavRail) {
                    SeanimeNavRail(
                        destinations = TopLevelDestination.entries,
                        onNavigateToDestination = { navigator.navigateToTopLevel(it.navKey) },
                        currentDestination = appState.navState.currentTopLevelKey
                    )
                }

                val entryProvider = context(navigator) {
                    entryProvider {
                        homeEntry()
                        searchEntry()
                        listEntry()
                        calendarEntry()

                        titleDetailsEntry()
                        titleAuthorsEntry()
                        titleCharactersEntry()
                        titleRelatedEntry()
                        animeScreenshotsEntry()
                        animeVideosEntry()

                        characterEntry()
                        profileEntry()

                        imageViewEntry()
                        //rateEditorEntry()
                    }
                }

                NavDisplay(
                    backStack = appState.navState.currentSubStack,
                    modifier = Modifier.weight(1f),
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    entryProvider = entryProvider,
                    onBack = navigator::back
                )
            }
        }

        if (editableUserRate != null) {
            //UserRateBottomSheet(
            //    editableUserRate = checkNotNull(editableUserRate),
            //    onDismissRequest = { editableUserRate = null }
            //)
        }

        if (fullScreenImageState.isVisible) {
            FullScreenImageView(
                state = fullScreenImageState,
                onDismissRequest = { }
            )
        }
    }
}

@Composable
private fun SeanimeNavRail(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavKey,
    modifier: Modifier = Modifier
) {
    NavigationRail(modifier = modifier) {
        destinations.fastForEach {
            val selected = currentDestination == it.navKey
            NavigationRailItem(
                selected = selected,
                onClick = { onNavigateToDestination(it) },
                icon = {
                    if (selected) {
                        Icon(
                            imageVector = it.selectedIcon,
                            contentDescription = stringResource(id = it.iconTextId)
                        )
                    } else {
                        Icon(
                            imageVector = it.unselectedIcon,
                            contentDescription = stringResource(id = it.iconTextId)
                        )
                    }
                },
                label = { Text(stringResource(id = it.titleTextId)) }
            )
        }
    }
}

@Composable
private fun SeanimeBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavKey,
    modifier: Modifier = Modifier
) {
    BottomAppBar(modifier = modifier) {
        destinations.fastForEach {
            val selected = it.navKey == currentDestination
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(it) },
                icon = {
                    if (selected) {
                        Icon(
                            imageVector = it.selectedIcon,
                            contentDescription = stringResource(id = it.iconTextId)
                        )
                    } else {
                        Icon(
                            imageVector = it.unselectedIcon,
                            contentDescription = stringResource(id = it.iconTextId)
                        )
                    }
                },
                label = { Text(stringResource(id = it.titleTextId)) }
            )
        }
    }
}
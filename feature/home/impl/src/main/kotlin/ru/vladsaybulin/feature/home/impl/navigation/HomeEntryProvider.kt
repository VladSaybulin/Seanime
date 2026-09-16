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

package ru.vladsaybulin.feature.home.impl.navigation

import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.core.navigation.SeanimeNavKey
import ru.vladsaybulin.feature.home.api.navigation.HomeNavKey
import ru.vladsaybulin.feature.home.impl.HomeScreen
import ru.vladsaybulin.feature.home.impl.HomeViewModel
import ru.vladsaybulin.feature.list.title.details.navigation.navigateToAnime
import ru.vladsaybulin.feature.list.title.details.navigation.navigateToManga
import ru.vladsaybulin.feature.profile.api.navigation.navigateToProfile
import ru.vladsaybulin.feature.rate.editor.api.navigation.navigateToRateEditor
import ru.vladsaybulin.feature.search.api.navigation.navigateToSearchOngoingTitles

context(navigator: Navigator)
fun EntryProviderScope<SeanimeNavKey>.homeEntry() = entry<HomeNavKey> {
    val viewModel = hiltViewModel<HomeViewModel>()
    val uriHandler = LocalUriHandler.current

    HomeScreen(
        viewModel = viewModel,
        onAllNewsTopicsClick = { uriHandler.openUri(TOPIC_URL) }, // Maybe infinity scroll in Home screen?
        onAnimeClick = navigator::navigateToAnime,
        onExploreAnimeOngoingClick = navigator::navigateToSearchOngoingTitles,
        onMangaClick = navigator::navigateToManga,
        onMyProfileClick = navigator::navigateToProfile,
        onRateClick = navigator::navigateToRateEditor,
        onTopicClick = { uriHandler.openUri(TOPIC_URL + it) },
        onUserClick = navigator::navigateToProfile,
    )
}

private const val TOPIC_URL = "https://shikimori.io/news/"
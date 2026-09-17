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

package ru.vladsaybulin.feature.title.screenshots.impl.navigator

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.core.navigation.SeanimeNavKey
import ru.vladsaybulin.feature.imageview.api.navigation.ImageViewSource
import ru.vladsaybulin.feature.imageview.api.navigation.showFullScreenImageSet
import ru.vladsaybulin.feature.title.screenshots.api.navigation.AnimeScreenshotsNavKey
import ru.vladsaybulin.feature.title.screenshots.impl.AnimeScreenshotsRoute
import ru.vladsaybulin.feature.title.screenshots.impl.AnimeScreenshotsViewModel

context(navigator: Navigator)
fun EntryProviderScope<SeanimeNavKey>.animeScreenshotsEntry() = entry<AnimeScreenshotsNavKey> { key ->
    val viewModel = hiltViewModel<AnimeScreenshotsViewModel, AnimeScreenshotsViewModel.Factory> {
        it.create(key)
    }

    AnimeScreenshotsRoute(
        viewModel = viewModel,
        onScreenshotClick = { startIdx: Int, setSize: Int, startUrl: String ->
            val source = ImageViewSource.AnimeScreenshots(key.animeId)
            navigator.showFullScreenImageSet(
                source = source,
                startImageIndex = startIdx,
                imageSetSize = setSize,
                startImageUrl = startUrl
            )
        },
        onBackClick = navigator::back
    )
}
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

package ru.vladsaybulin.feature.title.details.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.core.navigation.SeanimeNavKey
import ru.vladsaybulin.feature.character.api.navigation.navigateToCharacter
import ru.vladsaybulin.feature.imageview.api.navigation.ImageViewSource
import ru.vladsaybulin.feature.imageview.api.navigation.showFullScreenImage
import ru.vladsaybulin.feature.imageview.api.navigation.showFullScreenImageSet
import ru.vladsaybulin.feature.list.title.details.navigation.TitleDetailsNavKey
import ru.vladsaybulin.feature.list.title.details.navigation.navigateToAnime
import ru.vladsaybulin.feature.list.title.details.navigation.navigateToManga
import ru.vladsaybulin.feature.rate.editor.api.navigation.navigateToRateEditor
import ru.vladsaybulin.feature.search.api.navigation.PresetSearchFilter
import ru.vladsaybulin.feature.search.api.navigation.navigateToSearchByFilter
import ru.vladsaybulin.feature.title.authors.api.navigation.navigateToTitleAuthors
import ru.vladsaybulin.feature.title.characters.api.navigation.navigateToTitleCharacters
import ru.vladsaybulin.feature.title.details.impl.TitleDetailsScreen
import ru.vladsaybulin.feature.title.details.impl.TitleDetailsViewModel
import ru.vladsaybulin.feature.title.related.api.navigation.navigateToTitleRelated
import ru.vladsaybulin.feature.title.screenshots.api.navigation.navigateToAnimeScreenshots
import ru.vladsaybulin.feature.title.videos.api.navigation.navigateToAnimeVideos
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.search.SearchType

context(navigator: Navigator)
fun EntryProviderScope<SeanimeNavKey>.titleDetailsEntry() = entry<TitleDetailsNavKey> { key ->
    val viewModel = hiltViewModel<TitleDetailsViewModel, TitleDetailsViewModel.Factory> {
        it.create(key)
    }

    TitleDetailsScreen(
        viewModel = viewModel,
        onAllAuthorsClick = { navigator.navigateToTitleAuthors(key.titleType, key.titleId) },
        onAllCharactersClick = { navigator.navigateToTitleCharacters(key.titleType, key.titleId) },
        onAllRelatedClick = { navigator.navigateToTitleRelated(key.titleType, key.titleId) },
        onAllScreenshotsClick = { navigator.navigateToAnimeScreenshots(key.titleId) },
        onAllVideosClick = { navigator.navigateToAnimeVideos(key.titleId) },
        onAnimeClick = navigator::navigateToAnime,
        onCharacterClick = navigator::navigateToCharacter,
        onGenreClick = { searchType, genreId ->
            val preset = PresetSearchFilter(PresetSearchFilter.Field.Genre, genreId)
            navigator.navigateToSearchByFilter(searchType, preset)
        },
        onMangaClick = navigator::navigateToManga,
        onPersonClick = {},
        onPosterClick = navigator::showFullScreenImage,
        onPublisherClick = { searchType, publisherId ->
            val preset = PresetSearchFilter(PresetSearchFilter.Field.Publisher, publisherId)
            navigator.navigateToSearchByFilter(searchType, preset)
        },
        onRateClick = navigator::navigateToRateEditor,
        onScreenshotClick = { setSize, startIndex, startUrl ->
            val source = if (key.titleType == EntryType.Anime) {
                ImageViewSource.AnimeScreenshots(key.titleId)
            } else null

            source?.let {
                navigator.showFullScreenImageSet(
                    source = it,
                    startImageIndex = startIndex,
                    imageSetSize = setSize,
                    startImageUrl = startUrl
                )
            }
        },
        onStudioClick = { studioId ->
            val preset = PresetSearchFilter(PresetSearchFilter.Field.Studio, studioId)
            navigator.navigateToSearchByFilter(SearchType.Anime, preset)
        },
        onBackClick = navigator::back
    )
}
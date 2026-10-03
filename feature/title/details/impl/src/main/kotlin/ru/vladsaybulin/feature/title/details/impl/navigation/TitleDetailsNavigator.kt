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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.feature.character.api.navigation.navigateToCharacter
import ru.vladsaybulin.feature.imageview.api.navigation.ImageViewSource
import ru.vladsaybulin.feature.imageview.api.navigation.navigateToImageView
import ru.vladsaybulin.feature.list.title.details.navigation.TitleDetailsNavKey
import ru.vladsaybulin.feature.list.title.details.navigation.navigateToAnime
import ru.vladsaybulin.feature.list.title.details.navigation.navigateToManga
import ru.vladsaybulin.feature.list.title.details.navigation.navigateToTitle
import ru.vladsaybulin.feature.rate.editor.api.navigation.TitleReference
import ru.vladsaybulin.feature.rate.editor.api.navigation.navigateToRateEditor
import ru.vladsaybulin.feature.search.api.navigation.PresetSearchFilter
import ru.vladsaybulin.feature.search.api.navigation.navigateToSearchByFilter
import ru.vladsaybulin.feature.title.authors.api.navigation.navigateToTitleAuthors
import ru.vladsaybulin.feature.title.characters.api.navigation.navigateToTitleCharacters
import ru.vladsaybulin.feature.title.related.api.navigation.navigateToTitleRelated
import ru.vladsaybulin.feature.title.screenshots.api.navigation.navigateToAnimeScreenshots
import ru.vladsaybulin.feature.title.videos.api.navigation.navigateToAnimeVideos
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.search.SearchType
import ru.vladsaybulin.model.title.Title
import ru.vladsaybulin.model.userrate.UserRateContext
import ru.vladsaybulin.model.userrate.UserRateValues

class TitleDetailsNavigator(
    val onAllAuthorsClick: () -> Unit,
    val onAllCharactersClick: () -> Unit,
    val onAllRelatedClick: () -> Unit,
    val onAllScreenshotsClick: () -> Unit,
    val onAllVideosClick: () -> Unit,
    val onAnimeClick: (Long) -> Unit,
    val onCharacterClick: (Long) -> Unit,
    val onGenreClick: (SearchType, Long) -> Unit,
    val onMangaClick: (Long) -> Unit,
    val onPersonClick: (Long) -> Unit,
    val onPosterClick: (String) -> Unit,
    val onPublisherClick: (SearchType, Long) -> Unit,
    val onRateClick: (Long?, UserRateValues?, UserRateContext?) -> Unit,
    val onScreenshotClick: (images: List<String>, startIndex: Int) -> Unit,
    val onStudioClick: (Long) -> Unit,
    val onTitleClick: (Title) -> Unit,
    val onBackClick: () -> Unit
)

@Composable
fun rememberTitleDetailsNavigator(
    appNavigator: Navigator,
    key: TitleDetailsNavKey
) = remember(key) {
    TitleDetailsNavigator(
        onAllAuthorsClick = { appNavigator.navigateToTitleAuthors(key.titleType, key.titleId) },
        onAllCharactersClick = { appNavigator.navigateToTitleCharacters(key.titleType, key.titleId) },
        onAllRelatedClick = { appNavigator.navigateToTitleRelated(key.titleType, key.titleId) },
        onAllScreenshotsClick = { appNavigator.navigateToAnimeScreenshots(key.titleId) },
        onAllVideosClick = { appNavigator.navigateToAnimeVideos(key.titleId) },
        onAnimeClick = appNavigator::navigateToAnime,
        onCharacterClick = appNavigator::navigateToCharacter,
        onGenreClick = { searchType, genreId ->
            val preset = PresetSearchFilter(PresetSearchFilter.Field.Genre, genreId)
            appNavigator.navigateToSearchByFilter(searchType, preset)
        },
        onMangaClick = appNavigator::navigateToManga,
        onPersonClick = {},
        onPosterClick = { posterUrl ->
            val source = ImageViewSource.TitlePoster(key.titleType, key.titleId)
            appNavigator.navigateToImageView(source = source, startImageIndex = 0, loadedImages = listOf(posterUrl))
        },
        onPublisherClick = { searchType, publisherId ->
            val preset = PresetSearchFilter(PresetSearchFilter.Field.Publisher, publisherId)
            appNavigator.navigateToSearchByFilter(searchType, preset)
        },
        onRateClick = { rateId, values, context ->
            appNavigator.navigateToRateEditor(
                rateId = rateId,
                titleReference = key.titleReference(),
                rateValues = values,
                context = context
            )
        },
        onScreenshotClick = { images, startIndex ->
            val source = if (key.titleType == EntryType.Anime) {
                ImageViewSource.AnimeScreenshots(key.titleId)
            } else null

            source?.let {
                appNavigator.navigateToImageView(
                    source = it,
                    startImageIndex = startIndex,
                    loadedImages = images,
                )
            }
        },
        onStudioClick = { studioId ->
            val preset = PresetSearchFilter(PresetSearchFilter.Field.Studio, studioId)
            appNavigator.navigateToSearchByFilter(SearchType.Anime, preset)
        },
        onTitleClick = appNavigator::navigateToTitle,
        onBackClick = appNavigator::back
    )
}

val IdleNavigator = TitleDetailsNavigator(
    onAllAuthorsClick = {},
    onAllCharactersClick = {},
    onAllRelatedClick = {},
    onAllScreenshotsClick = {},
    onAllVideosClick = {},
    onAnimeClick = {},
    onCharacterClick = {},
    onGenreClick = { _, _ -> },
    onMangaClick = {},
    onPersonClick = {},
    onPosterClick = {},
    onPublisherClick = { _, _ -> },
    onRateClick = { _, _, _ -> },
    onScreenshotClick = { _, _ -> },
    onStudioClick = {},
    onTitleClick = {},
    onBackClick = {}
)

private fun TitleDetailsNavKey.titleReference() = TitleReference(titleType, titleId)
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

package ru.vladsaybulin.feature.title.details.navigation

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.search.SearchType
import ru.vladsaybulin.model.userrate.EditableUserRate
import java.security.KeyStore.Entry

data class TitleDetailsNavEvents(
    val navigateToTitleDetails: (EntryType, Long) -> Unit,
    val navigateToCharacterDetails: (Long) -> Unit,
    val navigateToPersonDetails: (Long) -> Unit,
    val navigateToTitleAuthors: (EntryType, Long) -> Unit,
    val navigateToTitleRelated: (EntryType, Long) -> Unit,
    val navigateToTitleCharacters: (EntryType, Long) -> Unit,
    val navigateToTitleScreenshots: (EntryType, Long) -> Unit,
    val navigateToTitleVideos: (EntryType, Long) -> Unit,
    val navigateToSearchByGenre: (type: SearchType, kind: GenreKind, genreId: Long) -> Unit,
    val navigateToSearchByStudio: (SearchType, studioId: Long) -> Unit,
    val navigateToSearchByPublisher: (SearchType, publisherId: Long) -> Unit,
    val navigateUp: () -> Unit,
    val showUserRateEditor: (EditableUserRate) -> Unit,
    val showFullScreenImage: (allImages: List<Image>, initialImageIndex: Int) -> Unit,
    val authWithShikimori: () -> Unit
)

/**
 * TitleDetailsNavEvents used for preview
 */
internal val IdleTitleDetailsNavEvents = TitleDetailsNavEvents(
    navigateToTitleDetails = { _, _ -> },
    navigateToCharacterDetails = { },
    navigateToPersonDetails = { },
    navigateToTitleAuthors = { _, _ -> },
    navigateToTitleRelated = { _, _ -> },
    navigateToTitleCharacters = { _, _ -> },
    navigateToTitleScreenshots = { _, _ -> },
    navigateToTitleVideos = { _, _ -> },
    navigateToSearchByGenre = { _, _, _ -> },
    navigateToSearchByStudio = { _, _ -> },
    navigateToSearchByPublisher = { _, _ -> },
    navigateUp = { },
    showUserRateEditor = { },
    showFullScreenImage = { _, _ -> },
    authWithShikimori = { }
)
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

package ru.vladsaybulin.feature.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.search.SearchScreen
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.search.SearchType

@Serializable
object SearchGraphRoute

@Serializable
internal data class SearchScreenRoute(
    val searchType: SearchType? = null,
    val entryStatus: EntryStatus? = null,
    val genreKind: GenreKind? = null,
    val genreId: Long? = null,
    val studioId: Long? = null,
    val publisherId: Long? = null
)

fun NavController.navigateToSearchGraph(navOptions: NavOptions?) {
    navigate(SearchGraphRoute, navOptions)
}

fun NavController.navigateToSearchScreenByStatus(searchType: SearchType, entryStatus: EntryStatus) {
    navigate(SearchScreenRoute(searchType = searchType, entryStatus = entryStatus))
}

fun NavController.navigateToSearchByGenre(searchType: SearchType, genreKind: GenreKind, genreId: Long) {
    navigate(SearchScreenRoute(searchType = searchType, genreKind = genreKind, genreId = genreId))
}

fun NavController.navigateToSearchScreenByStudioOrPublisher(searchType: SearchType, studioOrPublisherId: Long) {
    navigate(
        SearchScreenRoute(
            searchType = searchType,
            studioId = if (searchType == SearchType.Anime) studioOrPublisherId else null,
            publisherId = if (searchType != SearchType.Anime) studioOrPublisherId else null
        )
    )
}

fun NavGraphBuilder.searchGraph(
    navEvents: SearchNavEvents,
    nested: NavGraphBuilder.() -> Unit
) {
    navigation<SearchGraphRoute>(startDestination = SearchScreenRoute()) {
        searchScreen(navEvents)
        nested()
    }
}

fun NavGraphBuilder.searchScreen(navEvents: SearchNavEvents) {
    composable<SearchScreenRoute> {
        SearchScreen(navEvents = navEvents)
    }
}
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

package ru.vladsaybulin.core.domain

import ru.vladsaybulin.core.domain.repository.FiltersRepository
import ru.vladsaybulin.model.search.Filters
import ru.vladsaybulin.model.search.SearchType
import javax.inject.Inject

class GetSearchFiltersUseCase @Inject constructor(
    private val filtersRepository: FiltersRepository
) {
    suspend operator fun invoke(
        searchType: SearchType,
        studioEnabled: Boolean = true,
        publisherEnabled: Boolean = true,
        genreEnabled: Boolean = true,
        themesEnabled: Boolean = true,
        demographicEnabled: Boolean = true
    ): Filters = when (searchType) {
        SearchType.Anime -> filtersRepository.getAnimeFilters()
        SearchType.Manga -> filtersRepository.getMangaFilters()
        SearchType.Ranobe -> filtersRepository.getRanobeFilters()
    }.let {
        if (studioEnabled && publisherEnabled && genreEnabled && themesEnabled && demographicEnabled) it
        else it.copy(
            studiosOptions = if (studioEnabled) it.studiosOptions else null,
            publishersOptions = if (publisherEnabled) it.publishersOptions else null,
            genresOption = if (genreEnabled) it.genresOption else null,
            themesOptions = if (themesEnabled) it.genresOption else null,
            demographicOptions = if (demographicEnabled) it.genresOption else null
        )
    }
}
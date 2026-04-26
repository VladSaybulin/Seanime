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

package ru.vladsaybulin.model.search

import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.userrate.UserRateStatus

data class Filters(
    val animeKindOptions: List<FilterOption<AnimeKind>>? = null,
    val mangaKindOptions: List<FilterOption<MangaKind>>? = null,
    val statusOptions: List<FilterOption<EntryStatus>>? = null,
    val timePeriodAiringFilterOptions: List<FilterOption<TimePeriodAiring>>? = null,
    val myListStatus: List<FilterOption<UserRateStatus>>? = null,
    val duration: List<FilterOption<Duration>>? = null,
    val ratingOptions: List<FilterOption<AnimeRating>>? = null,
    val score: List<FilterOption<Int>>? = null,
    val genresOption: List<FilterOption<Genre>>? = null,
    val themesOptions: List<FilterOption<Genre>>? = null,
    val demographicOptions: List<FilterOption<Genre>>? = null,
    val studiosOptions: List<FilterOption<Studio>>? = null,
    val publishersOptions: List<FilterOption<Publisher>>? = null
)


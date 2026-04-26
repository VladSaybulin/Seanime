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

package ru.vladsaybulin.core.ui.filters

import ru.vladsaybulin.model.search.FilterType

typealias AppliedOptionValues = Map<String, OptionValue>
typealias AppliedFilters = Map<FilterType, AppliedOptionValues>

fun AppliedFilters.kind() = getOrEmpty(FilterType.Kind)

fun AppliedFilters.status() = getOrEmpty(FilterType.Status)

fun AppliedFilters.myListStatus() = getOrEmpty(FilterType.MyListStatus)

fun AppliedFilters.duration() = getOrEmpty(FilterType.Duration)

fun AppliedFilters.season() = getOrEmpty(FilterType.Season)

fun AppliedFilters.rating() = getOrEmpty(FilterType.Rating)

fun AppliedFilters.genres() = getOrEmpty(FilterType.Genre)

fun AppliedFilters.studios() = getOrEmpty(FilterType.Studio)

fun AppliedFilters.publishers() = getOrEmpty(FilterType.Publisher)

private fun AppliedFilters.getOrEmpty(type: FilterType) = getOrDefault(type, emptyMap())
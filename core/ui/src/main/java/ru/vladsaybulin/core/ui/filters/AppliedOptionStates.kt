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
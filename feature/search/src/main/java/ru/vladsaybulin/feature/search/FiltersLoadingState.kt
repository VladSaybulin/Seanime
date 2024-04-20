package ru.vladsaybulin.feature.search

import ru.vladsaybulin.model.search.Filters

sealed class FiltersLoadingState {

    data object Loading : FiltersLoadingState()

    data class Success(val filters: Filters) : FiltersLoadingState()
}
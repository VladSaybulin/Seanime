package ru.vladsaybulin.feature.search

import ru.vladsaybulin.core.ui.filters.AppliedFilters
import ru.vladsaybulin.model.search.Filters

sealed class SearchFiltersUiState {

    data object Loading : SearchFiltersUiState()

    data class Success(
        val filters: Filters,
        val applied: AppliedFilters
    ) : SearchFiltersUiState()
}
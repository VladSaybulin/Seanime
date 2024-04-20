package ru.vladsaybulin.feature.search

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import ru.vladsaybulin.core.ui.filters.AppliedFilters
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.SearchType

@Immutable
data class SearchUiState(
    val currentSearchType: SearchType,
    val currentOrder: Order,
    val filtersLoadingState: FiltersLoadingState,
    val appliedFilters: AppliedFilters,
    val availableSearchTypes: ImmutableList<SearchType>,
    val availableOrders: ImmutableList<Order>,
    val title: SearchTitle
)
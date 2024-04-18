package ru.vladsaybulin.feature.list

import ru.vladsaybulin.data.model.RecentSearchQuery

sealed class RecentSearchQueriesUiState {
    data object Loading : RecentSearchQueriesUiState()

    data class Success(val recentQueries: List<RecentSearchQuery>) : RecentSearchQueriesUiState()
}
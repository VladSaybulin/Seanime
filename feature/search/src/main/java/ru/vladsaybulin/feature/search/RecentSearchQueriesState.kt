package ru.vladsaybulin.feature.search

import ru.vladsaybulin.data.model.RecentSearchQuery

sealed class RecentSearchQueriesState {
    data object Loading : RecentSearchQueriesState()

    data class Success(val recentQueries: List<RecentSearchQuery>) : RecentSearchQueriesState()
}
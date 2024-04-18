package ru.vladsaybulin.data.model

import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.search.RecentSearchQueryEntity

data class RecentSearchQuery(
    val query: String,
    val queriedDate: Instant
)

fun RecentSearchQueryEntity.asExternalModel() = RecentSearchQuery(
    query = query,
    queriedDate = queriedDate
)
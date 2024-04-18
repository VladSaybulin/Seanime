package ru.vladsaybulin.data.model

import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.search.RecentSearchQueryDbo

data class RecentSearchQuery(
    val query: String,
    val queriedDate: Instant
)

fun RecentSearchQueryDbo.asExternalModel() = RecentSearchQuery(
    query = query,
    queriedDate = queriedDate
)
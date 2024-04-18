package ru.vladsaybulin.database.models.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "recent_search_query")
class RecentSearchQueryEntity(

    @PrimaryKey
    @ColumnInfo("query")
    val query: String,

    @ColumnInfo("queried_at")
    val queriedDate: Instant
)
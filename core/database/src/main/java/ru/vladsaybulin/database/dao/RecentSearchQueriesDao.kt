package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.search.RecentSearchQueryDbo

@Dao
interface RecentSearchQueriesDao {

    @Query("SELECT * FROM recent_search_query ORDER BY queried_at DESC LIMIT :limit")
    fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQueryDbo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceSearchQueries(recentSearchQuery: RecentSearchQueryDbo)

    @Query("DELETE FROM recent_search_query")
    suspend fun deleteAllSearchQueries()

    @Query("DELETE FROM recent_search_query WHERE `query` = :query")
    suspend fun deleteSearchQuery(query: String)


}
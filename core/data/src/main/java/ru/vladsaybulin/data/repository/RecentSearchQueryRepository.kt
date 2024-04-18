package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.RecentSearchQuery
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.database.dao.RecentSearchQueriesDao
import ru.vladsaybulin.database.models.search.RecentSearchQueryDbo
import javax.inject.Inject

class RecentSearchQueryRepository @Inject constructor(
    private val recentSearchQueriesDao: RecentSearchQueriesDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {

    fun getRecentSearchQuery(limit: Int): Flow<List<RecentSearchQuery>> =
        recentSearchQueriesDao.getRecentSearchQueries(limit)
            .map {
                it.map(RecentSearchQueryDbo::asExternalModel)
            }
            .flowOn(ioDispatcher)

    suspend fun deleteRecentSearchQuery(query: String) {
        withContext(ioDispatcher) {
            recentSearchQueriesDao.deleteSearchQuery(query)
        }
    }

    suspend fun insertOrReplaceRecentSearchQuery(query: String) {
        withContext(ioDispatcher) {
            recentSearchQueriesDao.insertOrReplaceSearchQueries(
                RecentSearchQueryDbo(
                    query = query,
                    queriedDate = Clock.System.now()
                )
            )
        }
    }
}
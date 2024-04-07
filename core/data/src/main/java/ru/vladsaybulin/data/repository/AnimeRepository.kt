package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery
import ru.vladsaybulin.core.network.graphql.AnimeUserRateQuery
import ru.vladsaybulin.data.model.asDbo
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.asSimilarEntry
import ru.vladsaybulin.data.model.asUserRate
import ru.vladsaybulin.data.util.flowOf
import ru.vladsaybulin.database.ShikiDatabase
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.network.datasource.AnimeDataSource
import ru.vladsaybulin.network.datasource.UserRateDataSource
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val animeDataSource: AnimeDataSource,
    private val userRateDataSource: UserRateDataSource,
    private val database: ShikiDatabase,
    @Dispatcher(ShikiDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {
    fun getEntryDetails(animeId: Long): Flow<EntryDetails> = combine(
        flowOf { animeDataSource.getAnimeDetails(animeId) },
        flowOf { animeDataSource.getSimilarAnimes(animeId) },
        flowOf { userRateDataSource.getAnimeUserRate(animeId) }
    ) { details, similar, userRate ->
        if (userRate != null) {
            saveUserRate(details, userRate)
        }
        Pair(details, similar)
    }
        .combine(database.userRateDao.getAnimeUserRate(animeId)) { (details, similar), userRate ->
            EntryDetails(
                anime = details.asExternalModel(),
                manga = null,
                similarEntries = similar.map { it.asSimilarEntry() },
                userRate = userRate?.asUserRate()
            )
        }
        .flowOn(ioDispatcher)

    private suspend fun saveUserRate(animeDetails: AnimeDetailsQuery.Anime, userRate: AnimeUserRateQuery.UserRate) {
        database.animeDao.insertOrReplaceAnimeEntity(animeDetails.asDbo())
        database.userRateDao.insertOrReplaceUserRate(userRate.asDbo(animeDetails.id))
    }

}
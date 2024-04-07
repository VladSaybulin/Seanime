package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.core.network.graphql.MangaDetailsQuery
import ru.vladsaybulin.core.network.graphql.MangaUserRateQuery
import ru.vladsaybulin.data.model.asDbo
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.asSimilarEntry
import ru.vladsaybulin.data.model.asUserRate
import ru.vladsaybulin.data.util.flowOf
import ru.vladsaybulin.database.ShikiDatabase
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.network.datasource.MangaDataSource
import ru.vladsaybulin.network.datasource.UserRateDataSource
import javax.inject.Inject

class MangaRepository @Inject constructor(
    private val mangaDataSource: MangaDataSource,
    private val userRateDataSource: UserRateDataSource,
    private val database: ShikiDatabase,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    fun getEntryDetails(mangaId: Long): Flow<EntryDetails> = combine(
        flowOf { mangaDataSource.getMangaDetails(mangaId) },
        flowOf { mangaDataSource.getSimilarManga(mangaId) },
        flowOf { userRateDataSource.getMangaUserRate(mangaId) }
    ) { details, similar, userRate ->
        if (userRate != null) {
            saveUserRate(details, userRate)
        }
        Pair(details, similar)
    }
        .combine(database.userRateDao.getMangaUserRate(mangaId)) { (details, similar), userRate ->
            EntryDetails(
                anime = null,
                manga = details.asExternalModel(),
                similarEntries = similar.map { it.asSimilarEntry() },
                userRate = userRate?.asUserRate()
            )
        }
        .flowOn(ioDispatcher)

    private suspend fun saveUserRate(
        mangaDetails: MangaDetailsQuery.Manga,
        userRate: MangaUserRateQuery.UserRate
    ) {
        database.mangaDao.insertOrReplaceMangaEntity(mangaDetails.asDbo())
        database.userRateDao.insertOrReplaceUserRate(userRate.asDbo(mangaDetails.id))
    }
}
package ru.vladsaybulin.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.core.network.graphql.MangaDetailsQuery
import ru.vladsaybulin.core.network.graphql.MangaUserRateQuery
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.asSimilarEntry
import ru.vladsaybulin.data.model.asUserRate
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.data.util.AbstractShikimoriPagingSource
import ru.vladsaybulin.data.util.DefaultSearchPagingConfig
import ru.vladsaybulin.data.util.flowOf
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.model.manga.MangaWithUserRate
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.network.datasource.MangaDataSource
import ru.vladsaybulin.network.datasource.UserRateDataSource
import javax.inject.Inject

class MangaRepository @Inject constructor(
    private val mangaDataSource: MangaDataSource,
    private val userRateDataSource: UserRateDataSource,
    private val mangaDao: MangaDao,
    private val userRateDao: UserRateDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {

    fun getPagedManga(
        queryMap: Map<QueryMapKey, String>,
        pagingConfig: PagingConfig = DefaultSearchPagingConfig
    ) : Flow<PagingData<MangaWithUserRate>> = Pager(
        config = pagingConfig,
        pagingSourceFactory = { getPagedMangaPagingSource(queryMap) }
    )
        .flow
        .flowOn(ioDispatcher)

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
        .combine(userRateDao.getMangaUserRate(mangaId)) { (details, similar), userRate ->
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
        mangaDao.insertOrReplaceMangaEntity(mangaDetails.asEntity())
        userRateDao.insertOrReplaceUserRate(userRate.asEntity(mangaDetails.id))
    }

    private fun getPagedMangaPagingSource(queryMap: Map<QueryMapKey, String>) =
        object : AbstractShikimoriPagingSource<MangaWithUserRate>() {
            override suspend fun loadPage(
                pageNumber: Int,
                pageSize: Int
            ): LoadResult<Int, MangaWithUserRate> = try {
                val networkMangas = mangaDataSource.getManga(
                    page = pageNumber,
                    limit = pageSize,
                    queryMap = queryMap
                )
                val mangaEntities = networkMangas.map { it.asEntity() }
                val userRatesEntities = networkMangas.mapNotNull { it.userRateEntityShell() }

                if (mangaEntities.isNotEmpty()) {
                    mangaDao.insertOrReplaceMangas(mangaEntities)
                }

                if (userRatesEntities.isNotEmpty()) {
                    userRateDao.insertOrReplaceUserRates(userRatesEntities)
                }

                val mangas = networkMangas.map {
                    MangaWithUserRate(
                        manga = it.asExternalModel(),
                        userRate = it.userRate?.asExternalModel()
                    )
                }

                LoadResult.Page(
                    data = mangas,
                    nextKey = if (mangas.size == pageSize) pageNumber + 1 else null,
                    prevKey = null
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
}
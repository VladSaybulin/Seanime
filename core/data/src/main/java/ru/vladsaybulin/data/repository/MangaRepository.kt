package ru.vladsaybulin.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.asPOJO
import ru.vladsaybulin.data.model.asUserRate
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.data.util.AbstractShikimoriPagingSource
import ru.vladsaybulin.data.util.DefaultSearchPagingConfig
import ru.vladsaybulin.data.util.flowOf
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaDetails
import ru.vladsaybulin.model.manga.MangaWithUserRate
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.network.datasource.MangaDataSource
import ru.vladsaybulin.network.datasource.UserRateDataSource
import ru.vladsaybulin.network.models.NetworkUserRate
import ru.vladsaybulin.network.models.manga.NetworkMangaDetails
import javax.inject.Inject

class MangaRepository @Inject constructor(
    private val mangaDataSource: MangaDataSource,
    private val userRateDataSource: UserRateDataSource,
    private val mangaDao: MangaDao,
    private val userRateDao: UserRateDao,
    private val authRepository: AuthRepository,
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

    fun getMangaDetails(mangaId: Long): Flow<MangaDetails> {
        val detailsFlow = flowOf { mangaDataSource.getMangaDetails(mangaId) }
        val userRateFlow = authRepository.authState.map {
            if (it == ShikimoriAuthState.Authorized) {
                userRateDataSource.getAnimeUserRate(mangaId)
            } else null
        }
        return detailsFlow.combine(userRateFlow) { details, userRate ->
            if (userRate != null) {
                saveUserRate(details, userRate)
            }
            details.asExternalModel()
        }.flowOn(ioDispatcher)
    }

    fun getAnimeDetailsUserRate(animeId: Long) =
        userRateDao.getAnimeUserRate(animeId).map { it?.asUserRate() }

    fun getSimilarAnimes(animeId: Long): Flow<List<Manga>> =
        flowOf {
            mangaDataSource.getSimilarManga(animeId).map { it.asExternalModel() }
        }.flowOn(ioDispatcher)

    private suspend fun saveUserRate(
        mangaDetails: NetworkMangaDetails,
        userRate: NetworkUserRate
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
                val mangaEntities = networkMangas.map { it.asPOJO() }
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
package ru.vladsaybulin.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.asUserRate
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.data.util.AbstractShikimoriPagingSource
import ru.vladsaybulin.data.util.DefaultSearchPagingConfig
import ru.vladsaybulin.data.util.flowOf
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.OngoingAnimeDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.OngoingAnimeEntity
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeDetails
import ru.vladsaybulin.model.anime.AnimeWithUserRate
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.network.datasource.AnimeDataSource
import ru.vladsaybulin.network.datasource.UserRateDataSource
import ru.vladsaybulin.network.models.NetworkAnime
import ru.vladsaybulin.network.models.NetworkUserRate
import ru.vladsaybulin.network.models.anime.NetworkAnimeDetails
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val animeDataSource: AnimeDataSource,
    private val userRateDataSource: UserRateDataSource,
    private val animeDao: AnimeDao,
    private val userRateDao: UserRateDao,
    private val ongoingAnimeDao: OngoingAnimeDao,
    private val authRepository: AuthRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {

    fun getPagedAnime(
        queryMap: Map<QueryMapKey, String>,
        pagingConfig: PagingConfig = DefaultSearchPagingConfig
    ): Flow<PagingData<AnimeWithUserRate>> = Pager(
        config = pagingConfig,
        pagingSourceFactory = { getPagedAnimePagingSource(queryMap) }
    )
        .flow
        .flowOn(ioDispatcher)

    fun getOngoingAnime(limit: Int = 10): Flow<List<Anime>> =
        flowOf { ongoingAnimeDao.getOngoingAnime(limit) }
            .onStart { loadOngoingAnime(INITIAL_PAGE, limit, true) }
            .map { it.map(AnimeEntity::asExternalModel) }
            .flowOn(ioDispatcher)

    fun getAnimeDetails(animeId: Long): Flow<AnimeDetails> {
        val detailsFlow = flowOf { animeDataSource.getAnimeDetails(animeId) }
        val userRateFlow = authRepository.authState.map {
            if (it == ShikimoriAuthState.Authorized) {
                userRateDataSource.getAnimeUserRate(animeId)
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

    fun getSimilarAnimes(animeId: Long): Flow<List<Anime>> =
        flowOf {
            animeDataSource.getSimilarAnimes(animeId).map { it.asExternalModel() }
        }.flowOn(ioDispatcher)

    private suspend fun loadOngoingAnime(
        pageNumber: Int,
        pageSize: Int,
        isRefreshing: Boolean
    ): Boolean {
        val response = animeDataSource.getAnime(
            page = pageNumber,
            limit = pageSize,
            statusString = EntryStatus.Ongoing.serializedName,
            order = Order.Popularity,
            myListString = "!watching,!rewatching"
        )
        val animes = response.map(NetworkAnime::asEntity)
        val ongoingAnime = animes.mapIndexed { index, dbo ->
            OngoingAnimeEntity(
                animeId = dbo.id,
                order = pageNumber * pageSize + index
            )
        }

        if (isRefreshing) {
            ongoingAnimeDao.deleteAll()
        }
        animeDao.insertOrReplaceAnimes(animes)
        ongoingAnimeDao.insertAll(ongoingAnime)

        return animes.size < pageSize
    }

    private fun getPagedAnimePagingSource(queryMap: Map<QueryMapKey, String>) =
        object : AbstractShikimoriPagingSource<AnimeWithUserRate>() {
            override suspend fun loadPage(
                pageNumber: Int,
                pageSize: Int
            ): LoadResult<Int, AnimeWithUserRate> = try {
                val networkAnimes = animeDataSource.getAnime(
                    page = pageNumber,
                    limit = pageSize,
                    queryMap = queryMap
                )
                val animeEntities = networkAnimes.map { it.asEntity() }
                val userRatesEntities = networkAnimes.mapNotNull { it.userRateEntityShell() }

                if (animeEntities.isNotEmpty()) {
                    animeDao.insertOrReplaceAnimes(animeEntities)
                }

                if (userRatesEntities.isNotEmpty()) {
                    userRateDao.insertOrReplaceUserRates(userRatesEntities)
                }

                val animes = networkAnimes.map {
                    AnimeWithUserRate(
                        anime = it.asExternalModel(),
                        userRate = it.userRate?.asExternalModel()
                    )
                }

                LoadResult.Page(
                    data = animes,
                    nextKey = if (animes.size == pageSize) pageNumber + 1 else null,
                    prevKey = null
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

    private suspend fun saveUserRate(
        animeDetails: NetworkAnimeDetails,
        userRate: NetworkUserRate
    ) {
        animeDao.insertOrReplaceAnimeEntity(animeDetails.asEntity())
        userRateDao.insertOrReplaceUserRate(userRate.asEntity(animeId = animeDetails.id))
    }
}

private const val INITIAL_PAGE = 1
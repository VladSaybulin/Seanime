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
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery
import ru.vladsaybulin.core.network.graphql.AnimeUserRateQuery
import ru.vladsaybulin.data.model.asAnime
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.asSimilarEntry
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
import ru.vladsaybulin.model.anime.AnimeWithUserRate
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.network.datasource.AnimeDataSource
import ru.vladsaybulin.network.datasource.UserRateDataSource
import ru.vladsaybulin.network.models.NetworkAnime
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val animeDataSource: AnimeDataSource,
    private val userRateDataSource: UserRateDataSource,
    private val animeDao: AnimeDao,
    private val userRateDao: UserRateDao,
    private val ongoingAnimeDao: OngoingAnimeDao,
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
        .combine(userRateDao.getAnimeUserRate(animeId)) { (details, similar), userRate ->
            EntryDetails(
                anime = details.asExternalModel(),
                manga = null,
                similarEntries = similar.map { it.asSimilarEntry() },
                userRate = userRate?.asUserRate()
            )
        }
        .flowOn(ioDispatcher)

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
                        anime = it.asAnime(),
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
        animeDetails: AnimeDetailsQuery.Anime,
        userRate: AnimeUserRateQuery.UserRate
    ) {
        animeDao.insertOrReplaceAnimeEntity(animeDetails.asEntity())
        userRateDao.insertOrReplaceUserRate(userRate.asEntity(animeDetails.id))
    }
}

private const val INITIAL_PAGE = 1
package ru.vladsaybulin.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.CreateUserRateDto
import ru.vladsaybulin.data.model.animeEntityOrNullShells
import ru.vladsaybulin.data.model.asDto
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.mangaEntityOrNullShells
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.database.DatabaseTransactionRunner
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.models.userrate.PagedUserRateEntity
import ru.vladsaybulin.database.models.userrate.PopulatedPagedUserRate
import ru.vladsaybulin.database.models.userrate.PopulatedUserRate
import ru.vladsaybulin.database.models.userrate.asExternalModel
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateValues
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import ru.vladsaybulin.network.datasource.AnimeDataSource
import ru.vladsaybulin.network.datasource.UserRateDataSource
import ru.vladsaybulin.network.models.UserRateWithEntryDto
import javax.inject.Inject

class UserRateRepository @Inject constructor(
    private val userRateDataSource: UserRateDataSource,
    private val animeDataSource: AnimeDataSource,
    private val userRateDao: UserRateDao,
    private val animeDao: AnimeDao,
    private val mangaDao: MangaDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    private val userRepository: UserRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    fun getLastInProgressUserRates(): Flow<List<UserRateWithEntry>> =
        userRateDao.getLastInProgressUserRates(10)
            .onStart { loadLastInProgressUserRates(10) }
            .map { it.map(PopulatedUserRate::asExternalModel) }
            .flowOn(ioDispatcher)

    fun getPagedAnimeUserRates(
        status: UserRateStatus,
        config: PagingConfig = DefaultPagingConfig
    ) = getPagedUserRates(
        config = config,
        pagingSourceFactory = { userRateDao.getPagedAnimeUserRates(status) },
        getLastPage = { userRateDao.getLastAnimeUserRatesPage(status) },
        loadPage = { pageNumber ->
            userRateDataSource.getAnimeUserRates(
                page = pageNumber,
                limit = USER_RATES_PAGE_SIZE,
                status = status
            )
        }
    )

    fun getPagedMangaUserRates(
        status: UserRateStatus,
        config: PagingConfig = DefaultPagingConfig
    ) = getPagedUserRates(
        config = config,
        pagingSourceFactory = { userRateDao.getPagedMangaUserRates(status) },
        getLastPage = { userRateDao.getLastMangaUserRatesPage(status) },
        loadPage = { pageNumber ->
            userRateDataSource.getMangaUserRates(
                page = pageNumber,
                limit = USER_RATES_PAGE_SIZE,
                status = status
            )
        }
    )

    suspend fun createUserRate(
        entryType: EntryType,
        entryId: Long,
        userRateValues: UserRateValues
    ) {
        require(userRateValues.status != UserRateStatus.None)
        withContext(ioDispatcher) {
            val myId = userRepository.getMyId() ?: throw IllegalStateException("Not authorized")
            val response = try {
                userRateDataSource.createUserRate(
                    CreateUserRateDto(
                        userId = myId,
                        entryType = entryType,
                        entryId = entryId,
                        userRateValues = userRateValues
                    )
                )
            } catch (exception: Exception) {
                //TODO If UserRate exists then load and save remote UserRate
                throw exception
            }
            if (response != null) {
                databaseTransactionRunner {
                    userRateDao.insertOrReplaceUserRate(response.asEntity())
                    userRateDao.deleteOrderUserRateByStatus(userRateValues.status)
                }
            }
        }
    }

    suspend fun updateUserRate(userRateId: Long, userRateValues: UserRateValues) {
        withContext(ioDispatcher) {
            val response = userRateDataSource.updateUserRate(userRateId, userRateValues.asDto())
            if (response != null) {
                userRateDao.insertOrReplaceUserRate(response.asEntity())
            }
        }
    }

    suspend fun deleteUserRate(userRateId: Long) {
        withContext(ioDispatcher) {
            userRateDataSource.deleteUSerRate(userRateId)
            userRateDao.deleteUserRate(userRateId)
        }
    }

    private suspend fun loadLastInProgressUserRates(limit: Int) {
        val animeUserRates = animeDataSource.getAnime(
            page = 1,
            limit = limit,
            queryMap = mapOf(QueryMapKey.MyList to UserRateStatus.Watching.serializedName)
        )
        animeDao.insertOrReplaceAnimes(animeUserRates.map { it.asEntity() })
        userRateDao.insertOrReplaceUserRates(animeUserRates.mapNotNull { it.userRateEntityShell() })
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun getPagedUserRates(
        pagingSourceFactory: () -> PagingSource<Int, PopulatedPagedUserRate>,
        getLastPage: suspend () -> Int,
        loadPage: suspend (pageNumber: Int) -> List<UserRateWithEntryDto>,
        config: PagingConfig = DefaultPagingConfig
    ): Flow<PagingData<UserRateWithEntry>> = Pager(
        config = config,
        remoteMediator = object : RemoteMediator<Int, PopulatedPagedUserRate>() {
            override suspend fun load(
                loadType: LoadType,
                state: PagingState<Int, PopulatedPagedUserRate>
            ): MediatorResult {

                val pageToLoad = when (loadType) {
                    LoadType.REFRESH -> USER_RATES_FIRST_PAGE
                    LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                    LoadType.APPEND -> getLastPage() + 1
                }

                return try {
                    val response = loadPage(pageToLoad)
                    writeUserRatesPage(pageToLoad, response)

                    MediatorResult.Success(
                        endOfPaginationReached = response.size < state.config.pageSize
                    )
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    MediatorResult.Error(exception)
                }
            }
        },
        pagingSourceFactory = pagingSourceFactory
    )
        .flow
        .map { pagingData ->
            pagingData.map { it.asExternalModel() }
        }
        .flowOn(ioDispatcher)

    private suspend fun writeUserRatesPage(
        page: Int,
        userRates: List<UserRateWithEntryDto>
    ) {
        val userRatesEntities = userRates.map(UserRateWithEntryDto::userRateEntityShell)
        val animeEntities = userRates.mapNotNull(UserRateWithEntryDto::animeEntityOrNullShells)
        val mangaEntities = userRates.mapNotNull(UserRateWithEntryDto::mangaEntityOrNullShells)

        val order = userRates.mapIndexed { index, networkModel ->
            PagedUserRateEntity(
                userRateId = networkModel.networkUserRate.id,
                page = page,
                index = index
            )
        }

        databaseTransactionRunner {
            if (page == USER_RATES_FIRST_PAGE) {
                userRateDao.deleteAllOrderedUserRates()
            }
            animeDao.insertOrReplaceAnimes(animeEntities)
            mangaDao.insertOrReplaceMangas(mangaEntities)
            userRateDao.insertOrReplaceUserRates(userRatesEntities)
            userRateDao.insertUserRateOrder(order)
        }
    }

    companion object {
        const val USER_RATES_FIRST_PAGE = 1
        const val USER_RATES_PAGE_SIZE = 50

        val DefaultPagingConfig = PagingConfig(
            pageSize = USER_RATES_PAGE_SIZE,
            initialLoadSize = USER_RATES_PAGE_SIZE
        )
    }
}
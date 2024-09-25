package ru.vladsaybulin.data.repository

import android.util.Log
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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.data.model.CreateUserRateRequest
import ru.vladsaybulin.data.model.animeEntityOrNullShells
import ru.vladsaybulin.data.model.asDto
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.mangaEntityOrNullShells
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.database.DatabaseTransactionRunner
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.models.userrate.InProgressUserRateEntity
import ru.vladsaybulin.database.models.userrate.PagedUserRateEntity
import ru.vladsaybulin.database.models.userrate.PopulatedPagedUserRate
import ru.vladsaybulin.database.models.userrate.PopulatedUserRate
import ru.vladsaybulin.database.models.userrate.asExternalModel
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrderField
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateValues
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import ru.vladsaybulin.network.datasource.UserRateDataSource
import ru.vladsaybulin.network.models.userrate.NetworkUserRateWithTitle
import javax.inject.Inject

class UserRateRepository @Inject constructor(
    private val userRateDataSource: UserRateDataSource,
    private val userRateDao: UserRateDao,
    private val animeDao: AnimeDao,
    private val mangaDao: MangaDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    private val userRepository: UserRepository,
    private val auth: ShikimoriAuthorization,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    fun getInProgressUserRatesStream(limit: Int): Flow<List<UserRateWithEntry>> =
        userRateDao.getFirstInProgressUserRatesStream(limit)
            .map { it.map(PopulatedUserRate::asExternalModel) }

    fun getPagedAnimeUserRates(
        status: UserRateStatus,
        orderField: UserRateOrderField,
        order: UserRateOrder,
        config: PagingConfig = DefaultPagingConfig
    ) = getPagedUserRates(
        status = status,
        orderField = orderField,
        sortOrder = order,
        config = config,
        pagingSourceFactory = userRateDao::getPagedAnimeUserRates,
        getLastPage = userRateDao::getLastAnimeUserRatesPage,
        loadPage = userRateDataSource::getAnimeUserRates
    )

    fun getPagedMangaUserRates(
        status: UserRateStatus,
        orderField: UserRateOrderField,
        order: UserRateOrder,
        config: PagingConfig = DefaultPagingConfig
    ) = getPagedUserRates(
        status = status,
        orderField = orderField,
        sortOrder = order,
        config = config,
        pagingSourceFactory = userRateDao::getPagedMangaUserRates,
        getLastPage = userRateDao::getLastMangaUserRatesPage,
        loadPage = userRateDataSource::getMangaUserRates
    )

    fun getAnimeUserRateStream(animeId: Long): Flow<UserRate?> =
        auth.shikimoriAuthState.flatMapLatest { authState ->

            if (authState == ShikimoriAuthState.LOGGED_IN) {

                userRateDao.getAnimeUserRate(animeId)
                    .onStart {
                        userRateDataSource.getAnimeUserRate(animeId)?.run {
                            userRateDao.insertOrReplaceUserRate(asEntity(animeId = animeId))
                        }
                    }
                    .map { it?.asExternalModel() }
            } else flowOf(null)
        }

    fun getMangaUserRateStream(mangaId: Long): Flow<UserRate?> =
        auth.shikimoriAuthState.flatMapLatest { authState ->

            if (authState == ShikimoriAuthState.LOGGED_IN) {

                userRateDao.getMangaUserRate(mangaId)
                    .onStart {
                        userRateDataSource.getMangaUserRate(mangaId)?.run {
                            userRateDao.insertOrReplaceUserRate(asEntity(mangaId = mangaId))
                        }
                    }
                    .map { it?.asExternalModel() }
            } else flowOf(null)
        }

    fun getAllAnimeUserRateStatusesStream() = userRateDao.getAllAnimeUserRateStatusesStream()

    fun getAllMangaUserRateStatusesStream() = userRateDao.getAllMangaUserRateStatusesStream()

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
                    CreateUserRateRequest(
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
                val userRate = response.asEntity()
                val inProgressUserRateEntityOrNull = if (response.status == UserRateStatus.None) {
                    InProgressUserRateEntity(userRate.id)
                } else null

                databaseTransactionRunner {
                    userRateDao.insertOrReplaceUserRate(userRate)
                    userRateDao.deleteOrderUserRateByStatus(userRateValues.status)
                    inProgressUserRateEntityOrNull?.let { userRateDao.insertOrIgnoreInProgressUserRate(it) }
                }
            }
        }
    }

    suspend fun updateUserRate(userRateId: Long, userRateValues: UserRateValues) {
        withContext(ioDispatcher) {
            val response = userRateDataSource.updateUserRate(userRateId, userRateValues.asDto())

            if (response == null) {
                Log.e("UserRateRepository", "Update user rate failed")
                return@withContext
            } else {
                val inProgressUserRateEntityOrNull = if (response.status == UserRateStatus.None) {
                    InProgressUserRateEntity(userRateId)
                } else null
                userRateDao.updateUserRate(response.asEntity())
                inProgressUserRateEntityOrNull?.let { userRateDao.insertOrIgnoreInProgressUserRate(it) }
            }
        }
    }

    suspend fun deleteUserRate(userRateId: Long) {
        withContext(ioDispatcher) {
            userRateDataSource.deleteUSerRate(userRateId)
            userRateDao.deleteUserRate(userRateId)
        }
    }

    suspend fun refreshInProgressUserRates() {
        withContext(ioDispatcher) {
            val watchingDeferred = async(ioDispatcher) {
                userRateDataSource.getUserRates(
                    page = 1,
                    limit = 50,
                    status = UserRateStatus.Watching,
                    order = UserRateOrderField.UpdatedAt to UserRateOrder.Desc
                )
            }

            val rewatchingDeferred = async(ioDispatcher) {
                userRateDataSource.getUserRates(
                    page = 1,
                    limit = 50,
                    status = UserRateStatus.Rewatching,
                    order = UserRateOrderField.UpdatedAt to UserRateOrder.Desc
                )
            }

            val networkUserRates = watchingDeferred.await() + rewatchingDeferred.await()

            val userRateEntities = networkUserRates.map { it.asEntity() }
            val animeEntities = networkUserRates.mapNotNull { it.networkAnime?.asEntity() }
            val mangasEntities = networkUserRates.mapNotNull { it.networkManga?.asEntity() }

            val inProgressUserRateEntities = networkUserRates.map {
                InProgressUserRateEntity(userRateId = it.networkUserRate.id)
            }

            databaseTransactionRunner {
                userRateDao.deleteAllInProgressUserRates()
                animeDao.upsertAnimes(animeEntities)
                mangaDao.upsertMangas(mangasEntities)
                userRateDao.insertOrReplaceUserRates(userRateEntities)
                userRateDao.insertInProgressUserRates(inProgressUserRateEntities)
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun getPagedUserRates(
        status: UserRateStatus,
        orderField: UserRateOrderField,
        sortOrder: UserRateOrder,
        pagingSourceFactory: PagingSourceFactory,
        getLastPage: GetLastPage,
        loadPage: LoadPage,
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
                    LoadType.APPEND -> getLastPage(status, orderField, sortOrder) + 1
                }

                return try {
                    val response =
                        loadPage(pageToLoad, USER_RATES_PAGE_SIZE, status, orderField, sortOrder)
                    writeUserRatesPage(
                        orderField = orderField,
                        sortOrder = sortOrder,
                        page = pageToLoad,
                        userRates = response
                    )

                    MediatorResult.Success(
                        endOfPaginationReached = response.size < state.config.pageSize
                    )
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    MediatorResult.Error(exception)
                }
            }
        },
        pagingSourceFactory = { pagingSourceFactory(status, orderField, sortOrder) }
    )
        .flow
        .map { pagingData -> pagingData.map(PopulatedPagedUserRate::asExternalModel) }
        .flowOn(ioDispatcher)

    private suspend fun writeUserRatesPage(
        orderField: UserRateOrderField,
        sortOrder: UserRateOrder,
        page: Int,
        userRates: List<NetworkUserRateWithTitle>
    ) {
        val userRatesEntities = userRates.map(NetworkUserRateWithTitle::userRateEntityShell)
        val animeEntities = userRates.mapNotNull(NetworkUserRateWithTitle::animeEntityOrNullShells)
        val mangaEntities = userRates.mapNotNull(NetworkUserRateWithTitle::mangaEntityOrNullShells)

        val order = userRates.mapIndexed { index, networkModel ->
            PagedUserRateEntity(
                userRateId = networkModel.networkUserRate.id,
                page = page,
                index = index,
                order = sortOrder,
                orderField = orderField
            )
        }

        databaseTransactionRunner {
            if (page == USER_RATES_FIRST_PAGE) {
                userRateDao.deleteAllOrderedUserRates()
            }
            animeDao.upsertAnimes(animeEntities)
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

private typealias PagingSourceFactory = (
    status: UserRateStatus,
    orderField: UserRateOrderField,
    sortOrder: UserRateOrder
) -> PagingSource<Int, PopulatedPagedUserRate>

private typealias LoadPage = suspend (
    pageNumber: Int,
    limit: Int,
    status: UserRateStatus,
    orderField: UserRateOrderField,
    sortOrder: UserRateOrder
) -> List<NetworkUserRateWithTitle>

private typealias GetLastPage = suspend (
    status: UserRateStatus,
    orderField: UserRateOrderField,
    sortOrder: UserRateOrder
) -> Int
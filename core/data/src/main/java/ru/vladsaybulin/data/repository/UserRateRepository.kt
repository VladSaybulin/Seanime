package ru.vladsaybulin.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.CreateUserRateDto
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asDto
import ru.vladsaybulin.data.model.userRateDboShell
import ru.vladsaybulin.data.util.AbstractShikimoriRemoteMediator
import ru.vladsaybulin.database.ShikiDatabase
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.userrate.PopulatedUserRateDbo
import ru.vladsaybulin.database.models.userrate.UserRateEntity
import ru.vladsaybulin.database.models.userrate.UserRateOrderDbo
import ru.vladsaybulin.database.models.userrate.asExternalModel
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Manga
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.model.UserRateValues
import ru.vladsaybulin.model.UserRateWithEntry
import ru.vladsaybulin.network.datasource.UserRateDataSource
import ru.vladsaybulin.network.models.UserRateWithEntryDto
import javax.inject.Inject

class UserRateRepository @Inject constructor(
    private val userRateDataSource: UserRateDataSource,
    private val database: ShikiDatabase,
    private val userRepository: UserRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    fun getLastInProgressUserRates(): Flow<List<UserRateWithEntry>> =
        database.userRateDao.getLastInProgressUserRates(10)
            .map { it.map(PopulatedUserRateDbo::asExternalModel) }
            .flowOn(ioDispatcher)

    fun getPagedAnimeUserRates(
        status: UserRateStatus,
        config: PagingConfig = DefaultUserRatePagingConfig
    ) = getPagedUserRates(
        config = config,
        pagingSourceFactory = { database.userRateDao.getPagedAnimeUserRates(status) },
        load = { pageNumber, pageSize ->
            userRateDataSource.getAnimeUserRates(
                page = pageNumber,
                limit = pageSize,
                status = status
            )
        }
    )

    fun getPagedMangaUserRates(
        status: UserRateStatus,
        config: PagingConfig = DefaultUserRatePagingConfig
    ) = getPagedUserRates(
        config = config,
        pagingSourceFactory = { database.userRateDao.getPagedAnimeUserRates(status) },
        load = { pageNumber, pageSize ->
            userRateDataSource.getMangaUserRates(
                page = pageNumber,
                limit = pageSize,
                status = status
            )
        }
    )

    suspend fun createUserRate(userRateValues: UserRateValues, anime: Anime) {
        createUserRate(EntryType.Anime, anime.id, userRateValues) {
            database.animeDao.insertOrReplaceAnimeEntity(anime.asEntity())
        }
    }

    suspend fun createUserRate(userRateValues: UserRateValues, manga: Manga) {
        createUserRate(EntryType.Manga, manga.id, userRateValues) {
            database.mangaDao.insertOrReplaceMangaEntity(manga.asEntity())
        }
    }

    suspend fun updateUserRate(userRateId: Long, userRateValues: UserRateValues) {
        withContext(ioDispatcher) {
            val response = userRateDataSource.updateUserRate(userRateId, userRateValues.asDto())
            if (response != null) {
                database.userRateDao.insertOrReplaceUserRate(response.asEntity())
            }
        }
    }

    suspend fun deleteUserRate(userRateId: Long) {
        withContext(ioDispatcher) {
            userRateDataSource.deleteUSerRate(userRateId)
            database.userRateDao.deleteUserRate(userRateId)
        }
    }

    private suspend fun createUserRate(
        entryType: EntryType,
        entryId: Long,
        userRateValues: UserRateValues,
        onSaveEntity: suspend () -> Unit
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
                database.withTransaction {
                    onSaveEntity()
                    database.userRateDao.insertOrReplaceUserRate(response.asEntity())
                    database.userRateDao.deleteOrderUserRateByStatus(userRateValues.status)
                }
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun getPagedUserRates(
        pagingSourceFactory: () -> PagingSource<Int, PopulatedUserRateDbo>,
        load: suspend (pageNumber: Int, pageSize: Int) -> List<UserRateWithEntryDto>,
        config: PagingConfig = DefaultUserRatePagingConfig
    ): Flow<PagingData<UserRateWithEntry>> = Pager(
        config = config,
        remoteMediator = object : AbstractShikimoriRemoteMediator<PopulatedUserRateDbo>() {
            override suspend fun loadPage(
                pageNumber: Int,
                pageSize: Int,
                loadType: LoadType,
            ): MediatorResult {
                val response = load(pageNumber, pageSize)

                val userRates = mutableListOf<UserRateEntity>()
                val order = mutableListOf<UserRateOrderDbo>()
                val animes = mutableListOf<AnimeEntity>()
                val mangas = mutableListOf<MangaEntity>()

                val start = pageNumber * pageSize

                response.forEachIndexed { index, dto ->
                    userRates.add(dto.userRateDboShell())
                    dto.networkAnime?.let { animes.add(it.asEntity()) }
                    dto.networkManga?.let { mangas.add(it.asEntity()) }
                    order.add(UserRateOrderDbo(dto.userRateDto.id, start + index))
                }

                if (loadType == LoadType.REFRESH) {
                    database.userRateDao.deleteAllOrderedUserRates()
                }
                database.animeDao.insertOrReplaceAnimes(animes)
                database.mangaDao.insertOrReplaceMangas(mangas)
                database.userRateDao.insertOrReplaceUserRates(userRates)
                database.userRateDao.insertUserRateOrder(order)

                return MediatorResult.Success(endOfPaginationReached = userRates.size < pageSize)
            }
        },
        pagingSourceFactory = pagingSourceFactory
    )
        .flow
        .map { pagingData ->
            pagingData.map { it.asExternalModel() }
        }
        .flowOn(ioDispatcher)
}

private val DefaultUserRatePagingConfig = PagingConfig(
    pageSize = 50,
    enablePlaceholders = false,
    initialLoadSize = 50
)
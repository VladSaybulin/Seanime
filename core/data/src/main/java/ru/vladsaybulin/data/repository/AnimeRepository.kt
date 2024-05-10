package ru.vladsaybulin.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.AnimeDetailsEntityMapper
import ru.vladsaybulin.data.model.animeAuthorEntities
import ru.vladsaybulin.data.model.animeCharacterEntities
import ru.vladsaybulin.data.model.animeRelatedEntities
import ru.vladsaybulin.data.model.animeScreenshotEntities
import ru.vladsaybulin.data.model.animeStudioCrossRefs
import ru.vladsaybulin.data.model.animeVideoEntities
import ru.vladsaybulin.data.model.asAnimeEntity
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.asUserRate
import ru.vladsaybulin.data.model.characterEntityShells
import ru.vladsaybulin.data.model.genreEntityShells
import ru.vladsaybulin.data.model.genresCrossReferences
import ru.vladsaybulin.data.model.personEntityShells
import ru.vladsaybulin.data.model.relatedAnimeEntityShell
import ru.vladsaybulin.data.model.relatedMangaEntityShell
import ru.vladsaybulin.data.model.studioEntityShells
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.data.util.AbstractShikimoriPagingSource
import ru.vladsaybulin.data.util.DefaultSearchPagingConfig
import ru.vladsaybulin.data.util.flowOf
import ru.vladsaybulin.data.util.sync
import ru.vladsaybulin.database.DatabaseTransactionRunner
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.AnimeDetailsDao
import ru.vladsaybulin.database.dao.CharacterDao
import ru.vladsaybulin.database.dao.GenreDao
import ru.vladsaybulin.database.dao.LastRequestDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.OngoingAnimeDao
import ru.vladsaybulin.database.dao.PersonDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.OngoingAnimeEntity
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.database.models.lastrequest.LastAnimeDetailsRequestEntity
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
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.days

@Singleton
class AnimeRepository @Inject constructor(
    private val animeDataSource: AnimeDataSource,
    private val userRateDataSource: UserRateDataSource,
    private val animeDao: AnimeDao,
    private val userRateDao: UserRateDao,
    private val ongoingAnimeDao: OngoingAnimeDao,
    private val animeDetailsDao: AnimeDetailsDao,
    private val personDao: PersonDao,
    private val characterDao: CharacterDao,
    private val mangaDao: MangaDao,
    private val genreDao: GenreDao,
    private val lastRequestDao: LastRequestDao,
    private val authRepository: AuthRepository,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    private val animeDetailsMapper: Lazy<AnimeDetailsEntityMapper>,
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
        val userRateFlow = authRepository.authState.map {
            if (it == ShikimoriAuthState.LOGGED_IN) {
                userRateDataSource.getAnimeUserRate(animeId)
            } else null
        }
        return animeDetailsDao.getAnimeDetails(animeId)
            .onStart { syncAnimeDetails(animeId) }
            .map { it.asExternalModel() }
            .combine(userRateFlow) { details, userRate ->
                if (userRate != null) {
                    userRateDao.insertOrReplaceUserRate(userRate.asEntity(details.id))
                }
                details
            }
            .flowOn(ioDispatcher)
    }

    fun getAnimeDetailsUserRate(animeId: Long) =
        userRateDao.getAnimeUserRate(animeId).map { it?.asUserRate() }

    fun getSimilarAnimes(animeId: Long): Flow<List<Anime>> =
        flowOf {
            animeDataSource.getSimilarAnimes(animeId).map { it.asExternalModel() }
        }.flowOn(ioDispatcher)

    suspend fun refreshAnimeDetails(animeId: Long) {
        val animeDetails = animeDataSource.getAnimeDetails(animeId)
        databaseTransactionRunner {
            animeDetailsDao.insertOrReplaceStudios(animeDetails.studioEntityShells())
            animeDetails.genreEntityShells()?.let {
                genreDao.insertOrReplaceGenres(it)
            }
            animeDetails.characterEntityShells()?.let {
                characterDao.insertOrReplaceCharacters(it)
            }
            animeDetails.personEntityShells()?.let {
                personDao.insertOrReplacePersons(it)
            }
            animeDetails.relatedAnimeEntityShell()?.let {
                animeDao.insertOrReplaceAnimes(it)
            }
            animeDetails.relatedMangaEntityShell()?.let {
                mangaDao.insertOrReplaceMangas(it)
            }

            animeDao.insertOrReplaceAnime(animeDetails.asAnimeEntity())
            animeDetailsDao.insertOrReplaceAnimeDetails(animeDetailsMapper.get().invoke(animeDetails))

            animeDetailsDao.deleteAnimeStudioCrossReferences(animeId)
            animeDetails.animeStudioCrossRefs().let {
                animeDetailsDao.insertAnimeStudioCrossReferences(it)
            }
            animeDetailsDao.deleteAnimeGenreCrossReferences(animeId)
            animeDetails.genresCrossReferences()?.let {
                animeDetailsDao.insertAnimeGenreCrossReferences(it)
            }
            animeDetailsDao.deleteAnimeCharacters(animeId)
            animeDetails.animeCharacterEntities()?.let {
                animeDetailsDao.insertAnimeCharacters(it)
            }
            animeDetailsDao.deleteAnimeAuthors(animeId)
            animeDetails.animeAuthorEntities()?.let {
                animeDetailsDao.insertAnimeAuthors(it)
            }
            animeDetailsDao.deleteAnimeRelated(animeId)
            animeDetails.animeRelatedEntities()?.let {
                animeDetailsDao.insertAnimeRelated(it)
            }
            animeDetailsDao.deleteAnimeScreenshots(animeId)
            animeDetails.animeScreenshotEntities().let {
                animeDetailsDao.insertAnimeScreenshots(it)
            }
            animeDetailsDao.deleteAnimeVideos(animeId)
            animeDetails.animeVideoEntities()?.let {
                animeDetailsDao.insertAnimeVideos(it)
            }
        }
    }

    private suspend fun syncAnimeDetails(animeId: Long) {
        sync(
            ttl = 1.days,
            readLastUpdateDate = { lastRequestDao.getLastAnimeDetailsRequestDate(animeId) },
            updateLastRequest = {
                lastRequestDao.insertOrReplaceLastAnimeDetailsRequest(
                    LastAnimeDetailsRequestEntity(animeId, it)
                )
            },
            refresh = { refreshAnimeDetails(animeId) }
        )
    }

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
}

private const val INITIAL_PAGE = 1
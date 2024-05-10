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
import ru.vladsaybulin.data.model.MangaDetailsEntityMapper
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.asMangaEntity
import ru.vladsaybulin.data.model.asUserRate
import ru.vladsaybulin.data.model.characterEntityShells
import ru.vladsaybulin.data.model.genreEntityShells
import ru.vladsaybulin.data.model.genresCrossReferences
import ru.vladsaybulin.data.model.mangaAuthorEntities
import ru.vladsaybulin.data.model.mangaCharacterEntities
import ru.vladsaybulin.data.model.mangaPublisherCrossRefs
import ru.vladsaybulin.data.model.mangaRelatedEntities
import ru.vladsaybulin.data.model.personEntityShells
import ru.vladsaybulin.data.model.publisherEntityShells
import ru.vladsaybulin.data.model.relatedAnimeEntityShell
import ru.vladsaybulin.data.model.relatedMangaEntityShell
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.data.util.AbstractShikimoriPagingSource
import ru.vladsaybulin.data.util.DefaultSearchPagingConfig
import ru.vladsaybulin.data.util.flowOf
import ru.vladsaybulin.data.util.sync
import ru.vladsaybulin.database.DatabaseTransactionRunner
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.CharacterDao
import ru.vladsaybulin.database.dao.GenreDao
import ru.vladsaybulin.database.dao.LastRequestDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.MangaDetailsDao
import ru.vladsaybulin.database.dao.PersonDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.models.lastrequest.LastMangaDetailsRequestEntity
import ru.vladsaybulin.database.models.manga.asExternalModel
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaDetails
import ru.vladsaybulin.model.manga.MangaWithUserRate
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.network.datasource.MangaDataSource
import ru.vladsaybulin.network.datasource.UserRateDataSource
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class MangaRepository @Inject constructor(
    private val mangaDataSource: MangaDataSource,
    private val userRateDataSource: UserRateDataSource,
    private val animeDao: AnimeDao,
    private val userRateDao: UserRateDao,
    private val mangaDetailsDao: MangaDetailsDao,
    private val personDao: PersonDao,
    private val characterDao: CharacterDao,
    private val mangaDao: MangaDao,
    private val genreDao: GenreDao,
    private val lastRequestDao: LastRequestDao,
    private val authRepository: AuthRepository,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    private val mangaDetailsMapper: Lazy<MangaDetailsEntityMapper>,
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
        val userRateFlow = authRepository.authState.map {
            if (it == ShikimoriAuthState.LOGGED_IN) {
                userRateDataSource.getMangaUserRate(mangaId)
            } else null
        }
        return mangaDetailsDao.getMangaDetails(mangaId)
            .onStart { syncMangaDetails(mangaId) }
            .map { it.asExternalModel() }
            .combine(userRateFlow) { details, userRate ->
                if (userRate != null) {
                    userRateDao.insertOrReplaceUserRate(userRate.asEntity(mangaId = details.id))
                }
                details
            }
            .flowOn(ioDispatcher)
    }

    fun getMangaDetailsUserRate(mangaId: Long) =
        userRateDao.getMangaUserRate(mangaId).map { it?.asUserRate() }

    fun getSimilarMangas(mangaId: Long): Flow<List<Manga>> =
        flowOf {
            mangaDataSource.getSimilarManga(mangaId).map { it.asExternalModel() }
        }.flowOn(ioDispatcher)

    suspend fun refreshMangaDetails(mangaId: Long) {
        val mangaDetails = mangaDataSource.getMangaDetails(mangaId)
        databaseTransactionRunner {
            mangaDetailsDao.insertOrReplacePublishers(mangaDetails.publisherEntityShells())
            mangaDetails.genreEntityShells()?.let {
                genreDao.insertOrReplaceGenres(it)
            }
            mangaDetails.characterEntityShells()?.let {
                characterDao.insertOrReplaceCharacters(it)
            }
            mangaDetails.personEntityShells()?.let {
                personDao.insertOrReplacePersons(it)
            }
            mangaDetails.relatedAnimeEntityShell()?.let {
                animeDao.insertOrReplaceAnimes(it)
            }
            mangaDetails.relatedMangaEntityShell()?.let {
                mangaDao.insertOrReplaceMangas(it)
            }

            mangaDao.insertOrReplaceManga(mangaDetails.asMangaEntity())
            mangaDetailsDao.insertOrReplaceMangaDetails(mangaDetailsMapper.get().invoke(mangaDetails))

            mangaDetailsDao.deleteMangaPublisherCrossReferences(mangaId)
            mangaDetails.mangaPublisherCrossRefs().let {
                mangaDetailsDao.insertMangaPublisherCrossReferences(it)
            }
            mangaDetailsDao.deleteMangaGenreCrossReferences(mangaId)
            mangaDetails.genresCrossReferences()?.let {
                mangaDetailsDao.insertMangaGenreCrossReferences(it)
            }
            mangaDetailsDao.deleteMangaCharacters(mangaId)
            mangaDetails.mangaCharacterEntities()?.let {
                mangaDetailsDao.insertMangaCharacters(it)
            }
            mangaDetailsDao.deleteMangaAuthors(mangaId)
            mangaDetails.mangaAuthorEntities()?.let {
                mangaDetailsDao.insertMangaAuthors(it)
            }
            mangaDetailsDao.deleteMangaRelated(mangaId)
            mangaDetails.mangaRelatedEntities()?.let {
                mangaDetailsDao.insertMangaRelated(it)
            }
        }
    }

    private suspend fun syncMangaDetails(mangaId: Long) {
        sync(
            ttl = 1.hours,
            readLastUpdateDate = { lastRequestDao.getLastMangaDetailsRequestDate(mangaId) },
            updateLastRequest = {
                lastRequestDao.insertOrReplaceLastMangaDetailsRequest(
                    LastMangaDetailsRequestEntity(mangaId, it)
                )
            },
            refresh = { refreshMangaDetails(mangaId) }
        )
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